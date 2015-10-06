import java.util.concurrent.*;

public class Primes
{
	private static int running;

	private static boolean isPrime(int n)
	{
		for(int i=2;i<=Math.sqrt(n);i++)
		{
			if(n%i==0)
			{
				return false;
			}
		}
		return true;
	}

	public static void main(String... args)
	{
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);

		int low = Integer.parseInt(args[0]);
		int high = Integer.parseInt(args[1]);

		for(int i=low;i<high;i++)
		{
			final int j=i;
			pool.submit(new Callable<Void>()
			{
				public Void call()
				{
					synchronized(Primes.class)
					{
						Primes.running++;
					}
					if(Primes.isPrime(j))
					{
						System.out.println(String.format("%d is prime",j));
					}
					synchronized(Primes.class)
					{
						Primes.running--;
					}
					return null;
				}
			});
		}

		pool.shutdown();

		Thread daemon = new Thread()
		{
			public void run()
			{
				while(true)
				{
					synchronized(Primes.class)
					{
						System.out.println(String.format("running: %d",Primes.running));
					}
					try
					{
						Thread.sleep(1000);
					}
					catch(Exception ex)
					{
					}
				}
			}
		};
		daemon.setDaemon(true);
		daemon.start();
	}
}

