@Description(description = "Wypisanie 'x' oraz 'y' podana ilosc razy")
public class XY implements ICallable
{
	@Override
	public String call(String x, String y)
	{
		
		int xx = Integer.parseInt(x);
		int yy = Integer.parseInt(y);
		
		
		StringBuilder result = new StringBuilder();
		
		for(int i = 0; i < xx; i++)
		{
			result.append("x");
		}
		
		for(int i = 0; i < yy; i++)
		{
			result.append("y");
		}
		
		
		return String.valueOf(result);
	}
}
