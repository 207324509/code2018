package cn.kenenjoy.queue;

import java.util.concurrent.*;

/**
 * 队列
 * Created by hefa on 2017/12/7.
 */
public class QueueSample {

    public static void main(String[] args) throws InterruptedException {
        /**
         * 没有容量的阻塞队列
         */
        SynchronousQueue<Person> synchronousQueue = new SynchronousQueue<Person>();
        /**
         * 使用多线程对SynchronousQueue模拟操作
         */
        new Thread(new MyRunnable1(synchronousQueue)).start();
        new Thread(new MyRunnable2(synchronousQueue)).start();

        /**
         * 有限队列，先进先出
         */
        ArrayBlockingQueue<Person> arrayBlockingQueue = new ArrayBlockingQueue<Person>(2);
        /**
         * 不要使用add(),在没有容量的情况下会抛异常。
         * 推荐使用put(),在没有容量的情况下会被阻塞。
         */
//        queueTest.arrayBlockingQueue.add(queueTest.person);
        arrayBlockingQueue.put(new Person(1, "zhang", 24));
        /**
         * 试图从空队列中提前元素将导致阻塞。
         */
        arrayBlockingQueue.poll();

        /**
         * 无限队列，默认容量大小为Integer.MAX_VALUE。如果指定了容量大小，那么它反映的特性跟ArrayBlockingQueue类似
         */
        LinkedBlockingQueue<Person> linkedBlockingQueue = new LinkedBlockingQueue<Person>(2);
        /**
         * 不要使用add(),在没有容量的情况下会抛异常。
         * 推荐使用put(),在没有容量的情况下会被阻塞。
         * 如果没有设置容量大小参数，插入N个对象都不会被阻塞
         */
//        linkedBlockingQueue.add(queueTest.person);
        linkedBlockingQueue.put(new Person(1, "zhang", 24));

        /**
         * 无限队列，双端队列，既可以从头部插入/取出元素，又可以从尾部插入/取出元素。
         */
        LinkedBlockingDeque<Person> linkedBlockingDeque = new LinkedBlockingDeque<Person>(2);
        /**
         * push，可以从队列的头部插入元素,在没有容量的情况下会抛异常。
         * poll，可以从队列的头部取出元素,在没有元素的情况下不会阻塞。
         */
        linkedBlockingDeque.push(new Person(1, "zhang", 24));
        linkedBlockingDeque.poll();
        /**
         * put，可以从队列的尾部插入元素,在没有容量的情况下会被阻塞。
         * pollLast，可以从队列的尾部插入元素,在没有元素的情况下不会阻塞。
         */
        linkedBlockingDeque.put(new Person(1, "zhang", 24));
        linkedBlockingDeque.pollLast();

        /**
         * 无限队列，按照优先级进行内部元素排序的无限队列。
         * 元素必须实现Comparable接口，但是并不保证除了队列头部以后的元素排序是一定正确的。
         * 在没有元素的情况下不会阻塞。
         */
        PriorityBlockingQueue<Person> priorityBlockingQueue = new PriorityBlockingQueue<Person>();
        priorityBlockingQueue.put(new Person(9, "九", 29));
        priorityBlockingQueue.put(new Person(1, "一", 21));
        priorityBlockingQueue.put(new Person(4, "四", 24));
        priorityBlockingQueue.put(new Person(3, "三", 23));
        System.out.println(priorityBlockingQueue.poll().toString());// 元素位置：9，3，4，1
        System.out.println(priorityBlockingQueue.poll().toString());// 元素位置：4，3，1
        System.out.println(priorityBlockingQueue.poll().toString());// 元素位置：3，1
        System.out.println(priorityBlockingQueue.poll().toString());// 元素位置：1

        /**
         * 无限队列，除了具有一般队列的先进先出特性，还具有一个阻塞特性
         * 当生产者将一个新元素插入队列后，生产者线程将会一直等待，知道某个消费线程将这个元素取走，反之亦然。
         */
        LinkedTransferQueue<Person> linkedTransferQueue = new LinkedTransferQueue<Person>();
        new Thread(new ProducerRunnable(linkedTransferQueue)).start();
        new Thread(new ConsumerRunnable(linkedTransferQueue)).start();

    }

    private static class MyRunnable1 implements Runnable {
        private SynchronousQueue<Person> synchronousQueue;

        public MyRunnable1(SynchronousQueue<Person> synchronousQueue) {
            this.synchronousQueue = synchronousQueue;
        }

        @Override
        public void run() {
            try {
                // 注意不要使用add()方法，这个队列没有容量，所以会抛出异常
//                queueTest.queue.add(queueTest.person);
                // 操作线程会在这里被阻塞，直到有其他线程取走这个对象
                synchronousQueue.put(new Person(1, "zhang", 24));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("对象已被取走");
        }
    }

    private static class MyRunnable2 implements Runnable {
        private SynchronousQueue<Person> synchronousQueue;

        public MyRunnable2(SynchronousQueue<Person> synchronousQueue) {
            this.synchronousQueue = synchronousQueue;
        }

        @Override
        public void run() {
            //取走对象线程不会被阻塞
            Person person = synchronousQueue.poll();
            System.out.println("取走对象:" + person.toString());
        }
    }


    private static class ProducerRunnable implements Runnable {
        private LinkedTransferQueue<Person> linkedTransferQueue;

        public ProducerRunnable(LinkedTransferQueue<Person> linkedTransferQueue) {
            this.linkedTransferQueue = linkedTransferQueue;
        }

        @Override
        public void run() {
            try {
                /**
                 * put方法不会阻塞线程
                 * transfer操作线程会在这里被阻塞，直到有其他线程取走这个对象
                 */
//                linkedTransferQueue.put(new Person(1, "zhang", 24));
                linkedTransferQueue.transfer(new Person(1, "zhang", 24));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("对象已被取走");
        }
    }

    private static class ConsumerRunnable implements Runnable {
        private LinkedTransferQueue<Person> linkedTransferQueue;

        public ConsumerRunnable(LinkedTransferQueue<Person> linkedTransferQueue) {
            this.linkedTransferQueue = linkedTransferQueue;
        }

        @Override
        public void run() {
            Person person = null;
            try {
                /**
                 * poll不会阻塞，使用take会被阻塞
                 */
//                person = linkedTransferQueue.poll();
                person = linkedTransferQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("取走对象:" + person.toString());
        }
    }

    /**
     * 元素类，配合PriorityBlockingQueue示例，实现Comparable接口地址
     */
    private static class Person implements Comparable<Person> {
        public Person(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        private int id;
        private String name;
        private int age;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }

        @Override
        public int compareTo(Person person) {
            return person.getId() - this.id;
        }
    }
}
