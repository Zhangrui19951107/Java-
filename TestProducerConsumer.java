
class Container{
    public int max; //定义容器最大容量
    public int currentNum;//定义容器当前容量

    public Container(int max){
        this.max = max;
        currentNum = 0;
    }
}
class Producer implements Runnable{
    public Container con;
    public Producer(Container con){
        this.con = con;
    }
    public void run(){
        while(true){
            synchronized(con){
                if(con.currentNum < con.max){//若当前容器不满，则可以生产
                    con.currentNum++;
                    System.out.println(" 生产者正在生产...+1, 当前产品数："+con.currentNum);
                    con.notify();//生产完则通知并释放锁
                }else if(con.currentNum == con.max){//
                    System.out.println("箱子已经饱和，生产者停止生产，正在等待消费...");
                    try {
                        con.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable{
    public Container con;
    public Consumer(Container con){
        this.con = con;
    }
    public void run(){
        while(true){
            synchronized(con){
                if(con.currentNum > 0 ){
                    con.currentNum--;
                    System.out.println(" 消费者正在消费...-1, 当前产品数："+con.currentNum);
                    con.notify();
                }else if(con.currentNum == 0){
                    System.out.println("箱子已经空了，消费者停止消费，正在等待生产...");
                    try {
                        con.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                Thread.sleep(140);//调节消费者频率，过快容易撑死~~
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
public class TestProducerConsumer{
    public static void main(String args[]){
        Container container = new Container(6);//定义箱子最大容量，此处为5
        Producer producer =  new Producer(container);//箱子中的苹果数要同步，所以将箱子对象引用作为形参传给生产者和消费者
        Consumer consumer = new Consumer(container);//

        new Thread(producer, "producer").start();//启动生产消费模式
        new Thread(consumer, "consumer").start();
    }
}