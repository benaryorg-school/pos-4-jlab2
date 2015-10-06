import java.util.concurrent.*;
import java.util.*;

public class Pi
{
	public static void main(String... args)
	{
		List<Future<List<Double>>> list = new ArrayList<Future<List<Double>>>();
		final int threads = Runtime.getRuntime().availableProcessors();
		ExecutorService pool = Executors.newFixedThreadPool(threads);

		for(int i=0;i<threads;i++)
		{
			final int j = i;
			list.add(pool.submit(new Callable<List<Double>>()
			{
				public List<Double> call()
				{
				/*
				 *
				 * 0 1  2  3
				 * 4 5  6  7
				 * 8 9 10 11
				 */
					List<Double> list = new ArrayList<Double>();
					for(int i=j;i<100000;i+=threads)
					{
						list.add(Math.pow(-1,i)/(2*i+1));
					}
					return list;
				}
			}));
		}

		pool.shutdown();

		double sum = 0;
		for(Future<List<Double>> f:list)
		{
			try
			{
				for(Double d:f.get())
				{
					sum += d;
				}
				f.get().clear();
			}
			catch(Exception ex)
			{
			}
		}
		sum *= 4;

		System.out.println(sum);
	}
}


