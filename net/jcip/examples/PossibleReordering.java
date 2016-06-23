package net.jcip.examples;

/**
 * PossibleReordering
 * <p/>
 * Insufficiently synchronized program that can have surprising results
 *
 * @author Brian Goetz and Tim Peierls
 */
public class PossibleReordering {
	static int x = 0, y = 0;
	static int a = 0, b = 0;
	static int cnt = 0;

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 10000; i++) reorder();
		System.out.println(cnt);
	}

	private static synchronized void reorder() throws InterruptedException {
		Thread init = new Thread(new Runnable() {
			@Override
			public void run() {
				x = 0;
				y = 0;
				a = 0;
				b = 0;
			}
		});
		Thread one = new Thread(new Runnable() {
			public void run() {
				a = 1;
				x = b;
			}
		});
		Thread other = new Thread(new Runnable() {
			public void run() {
				b = 1;
				y = a;
			}
		});
		init.start();
		init.join();

		one.start();
		other.start();
		one.join();
		other.join();
		if (x == 0 && y == 0) {
			System.out.println("( " + x + "," + y + ")");
			cnt++;
		}

	}
}
