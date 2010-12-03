/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.karevoll.sorting.parallel;

/**
 *
 * @author berengal
 */
public class Counter {

  private int count;
  public Counter(int count) {
    this.count = count;
  }

  public synchronized boolean dec() {
    count--;
    notifyAll();
    return count == 0;
  }

  public synchronized void waitZero() {
    while (count > 0) {
      try {
        wait();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

}
