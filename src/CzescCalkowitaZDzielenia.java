@Description(description = "Czesc calkowita z dzielenia 1 wartosci przez 2 wartosc")
public class CzescCalkowitaZDzielenia implements ICallable
{
	@Override
	public String call(String x, String y)
	{
		
		int xx = Integer.parseInt(x);
		int yy = Integer.parseInt(y);
		int result = xx/yy;
		
		return String.valueOf(result);
	}
}
