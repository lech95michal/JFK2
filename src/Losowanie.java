import java.util.Random;

@Description(description = "Losowanie liczby calkowitej z podanego zakresu")
public class Losowanie implements ICallable
{
	@Override
	public String call(String x, String y)
	{
		
		int xx = Integer.parseInt(x);
		int yy = Integer.parseInt(y);
		
		Random random = new Random();
		
		
		int result = random.nextInt(yy-xx)+xx;
		
		return String.valueOf(result);
	}
}
