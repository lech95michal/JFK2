import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main extends JFrame
{
	int szerokosc = 700;
	int wysokosc = 600;
	
	JPanel zawartosc;
	JButton przyciskLadowaniaJara;
	JButton przyciskWykonaniaMetody;
	JList<Class<?>> listaKlas;
	JLabel description;
	JLabel wartosc1;
	JLabel wartosc2;
	JTextField tekstPierwszejWartosci;
	JTextField tekstDrugiejWartosci;
	JLabel wynik;
	JLabel tekstWyniku;
	
	JFileChooser jfc;
	FileNameExtensionFilter fnef;
	
	String jarPath;
	JarFile jarFile;
	JarEntry jarEntry;
	
	String className;
	Class<?> klasa;
	Description desc;
	ICallable callable;
	
	DefaultListModel<Class<?>> listaModeliKlas;
	
	
	public Main()
	{
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(300,200, szerokosc, wysokosc);
		
		this.setVisible(true);
		zawartosc = new JPanel();
		
		listaModeliKlas = new DefaultListModel<>();
		zawartosc.setLayout(null);
		zawartosc.setBounds(0,0, szerokosc, wysokosc);
		description = new JLabel();
		description.setBounds(300,50,300,100);
		zawartosc.add(description);
		wartosc1 = new JLabel();
		wartosc1.setText("Wartosc1:");
		wartosc1.setBounds(270,200,75,25);
		tekstPierwszejWartosci = new JTextField();
		tekstPierwszejWartosci.setBounds(270,230,100,50);
		wartosc2 = new JLabel();
		wartosc2.setText("Wartosc2:");
		wartosc2.setBounds(270,300,75,25);
		tekstDrugiejWartosci = new JTextField();
		tekstDrugiejWartosci.setBounds(270,330,100,50);
		zawartosc.add(wartosc1);
		zawartosc.add(wartosc2);
		zawartosc.add(tekstPierwszejWartosci);
		zawartosc.add(tekstDrugiejWartosci);
		wynik = new JLabel();
		wynik.setText("Wynik dzialania:");
		wynik.setBounds(270,400,100,25);
		zawartosc.add(wynik);
		tekstWyniku = new JLabel();
		tekstWyniku.setBounds(270,430,300,50);
		zawartosc.add(tekstWyniku);
		przyciskLadowaniaJara = new JButton("Zaladuj plik .jar");
		przyciskLadowaniaJara.setBounds(0,0,szerokosc,30);
		przyciskLadowaniaJara.setBackground(Color.yellow);
		przyciskLadowaniaJara.addActionListener(e ->
		{
			jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jfc.setAcceptAllFileFilterUsed(false);
			fnef = new FileNameExtensionFilter("JAR", "jar");
			jfc.addChoosableFileFilter(fnef);
			jfc.showOpenDialog(null);
			jarPath = jfc.getSelectedFile().getAbsolutePath();
			try
			{
				jarFile = new JarFile(jarPath);
				Enumeration<JarEntry> entries = jarFile.entries();
				URL[] urls = {new URL("jar:file:" + jarPath + "!/")};
				URLClassLoader ucl = URLClassLoader.newInstance(urls);
				
				load:
				while(entries.hasMoreElements())
				{
					jarEntry = entries.nextElement();
					className = jarEntry.getName();
					
					if(jarEntry.isDirectory() || !className.endsWith(".class")) continue load;
					
					className = className.substring(0, className.length()-6).replace("/",",");
					
					klasa = ucl.loadClass(className);
					
					if(!klasa.isAnnotationPresent(Description.class)) continue load;
					
					desc = klasa.getAnnotation(Description.class);
					
					if(!ICallable.class.isAssignableFrom(klasa)) throw new Exception();
					callable = (ICallable) klasa.newInstance();
					if(null == callable) throw new Exception();
					listaModeliKlas.addElement(klasa);
				}
				jarFile.close();
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
		});
		zawartosc.add(przyciskLadowaniaJara);
		
		przyciskWykonaniaMetody = new JButton();
		przyciskWykonaniaMetody.setText("Execute");
		przyciskWykonaniaMetody.setBounds(450,250,100,50);
		przyciskWykonaniaMetody.addActionListener(e ->
		{
			try
			{
				callable = (ICallable) listaKlas.getSelectedValue().newInstance();
				tekstWyniku.setText(callable.call(tekstPierwszejWartosci.getText(), tekstDrugiejWartosci.getText()));
			} catch (InstantiationException | IllegalAccessException e1)
			{
				e1.printStackTrace();
			}
		});
		zawartosc.add(przyciskWykonaniaMetody);
		
		listaKlas = new JList<>(listaModeliKlas);
		listaKlas.setBounds(50,50,200,400);
		listaKlas.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(null != listaKlas.getSelectedValue()){
					desc = listaKlas.getSelectedValue().getAnnotation(Description.class);
					description.setText(desc.description());
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e)
			{
			
			}
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
			
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
			
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
			
			}
		});
		zawartosc.add(listaKlas);
		this.setContentPane(zawartosc);
	}

	public static void main(String[] args)
	{
		Main main = new Main();
	}
}
