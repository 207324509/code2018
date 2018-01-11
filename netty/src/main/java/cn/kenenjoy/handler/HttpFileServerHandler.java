package cn.kenenjoy.handler;

import cn.kenenjoy.util.DateUtils;
import cn.kenenjoy.util.DecimalUtil;
import cn.kenenjoy.util.FileComparator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Created by hefa on 2018/1/11.
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(HttpFileServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {
        if (!request.decoderResult().isSuccess()) {
            // 返回错误
            sendError(channelHandlerContext, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        if (request.method() != HttpMethod.GET) {
            // 返回错误
            sendError(channelHandlerContext, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        final String uri = request.uri();
        final String path = sanitizeUri(uri);

        if (path == null) {
            // 返回错误
            sendError(channelHandlerContext, HttpResponseStatus.FORBIDDEN);
            return;
        }

        File file = new File(path);
        if (file.isHidden() || !file.exists()) {
            // 返回错误
            sendError(channelHandlerContext, HttpResponseStatus.NOT_FOUND);
            return;
        }

        if (file.isDirectory()) {
            if (uri.endsWith("/")) {
                sendListing(channelHandlerContext, file);
            } else {
                sendRedirect(channelHandlerContext, uri + "/");
            }
            return;
        }

        if (!file.isFile()) {
            // 返回错误
            sendError(channelHandlerContext, HttpResponseStatus.FORBIDDEN);
            return;
        }


        RandomAccessFile randomAccessFile = null;
        try {
            // 以只读方式打开文件
            randomAccessFile = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            // 返回错误
            sendError(channelHandlerContext, HttpResponseStatus.NOT_FOUND);
            return;
        }

        long fileLength = randomAccessFile.length();
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpUtil.setContentLength(response, fileLength);
        setContentTypeHeader(response, file);

        if (HttpUtil.isKeepAlive(request)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        channelHandlerContext.write(response);
        ChannelFuture sendFileFuture = null;
        sendFileFuture = channelHandlerContext.write(new ChunkedFile(randomAccessFile, 0, fileLength, 8192), channelHandlerContext.newProgressivePromise());
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture) throws Exception {
                logger.info("Transfer complete.");
            }

            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                if (total < 0) {
                    logger.info("Transfer progress: " + progress);
                } else {
                    logger.info("Transfer progress: " + progress + "/" + total);
                }
            }
        });

        ChannelFuture lastContentFuture = channelHandlerContext.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!HttpUtil.isKeepAlive(request)) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }

    }

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    private String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            logger.error(e1.getMessage(), e1);
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e2) {
                logger.error(e2.getMessage(), e2);
                throw new Error();
            }
        }

        uri = uri.replace('/', File.separatorChar);
        if (uri.contains(File.separator + ".")
                || uri.contains("." + File.separator)
                || uri.startsWith(".") || uri.endsWith(".")
                || INSECURE_URI.matcher(uri).matches()) {
            return null;
        }

        return System.getProperty("user.dir") + File.separator + uri;
    }

    private static void sendListing(ChannelHandlerContext ctx, File dir) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");

        String dirPath = dir.getPath();
        StringBuilder buf = new StringBuilder();

        buf.append("<!DOCTYPE html>\r\n");
        buf.append("<html><head><title>");
        buf.append("目录:");
        buf.append(dirPath);

        buf.append("</title></head><body>\r\n");
        buf.append("<h3>");
        buf.append("目录：").append(dirPath);
        buf.append("</h3>");
        buf.append("<table>");
        buf.append("<tr>");
        buf.append("<td></td>");
        buf.append("<td><a href=\"#\">Name</a></td>");
        buf.append("<td><a href=\"#\">Last modified</a></td>");
        buf.append("<td><a href=\"#\">Size</a></td>");
        buf.append("<td><a href=\"#\">Description</a></td>");
        buf.append("</tr>");
        buf.append("<tr>");
        buf.append("<td><img src=\"/src/back.gif\" alt=\"[PARENTDIR]\"></td>");
        buf.append("<td><a href=\"..\">Parent Directory</a></td>");
        buf.append("<td></td>");
        buf.append("<td align=\"right\"> - </td>");
        buf.append("</tr>");


        List<FileComparator> list = new ArrayList<FileComparator>();

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            list.add(new FileComparator(files[i]));
        }

        Collections.sort(list);

        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            FileComparator fileComparator = (FileComparator) iterator.next();
            File f = fileComparator.getFile();
            if (f.isHidden() || !f.canRead()) {
                continue;
            }
            String name = f.getName();
            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }

            buf.append("<tr>");
            if (f.isDirectory()) {
                buf.append("<td><img src=\"/src/folder.gif\" alt=\"[DIR]\"></td>");
                buf.append("<td><a href=\"" + name + "\">" + name + "/</a></td>");
                buf.append("<td>" + DateUtils.getDateString(f.lastModified()) + "</td>");
                buf.append("<td align=\"right\"></td>");
            } else {
                String size = null;
                double bit = 1024.00;

                if (f.length() < 1024) {// 小于1KB,则直接显示
                    size = String.valueOf(f.length());
                } else if (f.length() < 1024 * 1024) {// 小于1MB
                    double d = f.length() / (bit);
                    size = DecimalUtil.formatDouble(d) + "KB";
                } else if (f.length() < 1024 * 1024 * 1024) {
                    double d = f.length() / (bit * bit);
                    size = DecimalUtil.formatDouble(d) + "MB";
                } else {
                    double d = f.length() / (bit * bit * bit);
                    size = DecimalUtil.formatDouble(d) + "GB";
                }

                buf.append("<td><img src=\"/src/text.gif\" alt=\"[TXT]\"></td>");
                buf.append("<td><a href=\"" + name + "\">" + name + "</a></td>");
                buf.append("<td>" + DateUtils.getDateString(f.lastModified()) + "</td>");
                buf.append("<td align=\"right\"> " + size + " </td>");
            }
            buf.append("</tr>");
        }

        buf.append("</table>");
        buf.append("</body></html>");

        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void sendRedirect(ChannelHandlerContext ctx, String uri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, uri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure: " + status.toString(), CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimetypesFileTypeMap.getContentType(file.getPath()));
    }
}
