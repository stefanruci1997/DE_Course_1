package crystal.training.concurency.false_sharing;

import sun.misc.Contended;

public final class FalseSharing implements Runnable {
  public final static int NUM_THREADS = 4; // change
  public final static long ITERATIONS = 50L * 1000L * 1000L;
  private final int arrayIndex;

  private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];

  static {
    for (int i = 0; i < longs.length; i++) {
      longs[i] = new VolatileLong();
    }
  }

  public FalseSharing(final int arrayIndex) {
    this.arrayIndex = arrayIndex;
  }


  private static void runTest() throws InterruptedException {
    Thread[] threads = new Thread[NUM_THREADS];

    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(new FalseSharing(i));
    }

    for (Thread t : threads) {
      t.start();
    }

    for (Thread t : threads) {
      t.join();
    }
  }

  public void run() {
    long i = ITERATIONS + 1;
    while (0!=--i) {
      longs[arrayIndex].value = i;
    }
  }

  public final static class VolatileLong {
    //@Contended // check the execution params and make it work ..
    public volatile long value = 0L;
    //public long p1, p2, p3, p4, p5, p6; // comment out

    //public long fakeOps() {
    // return p1 + p2 + p3 + p4 + p5 + p6;
    //}
  }

  public static void main(final String[] args) throws Exception {
    final long start = System.nanoTime();
    runTest();
    System.out.println("duration = " + (System.nanoTime() - start) / 1000_000.0);
  }
}
