package me.nico.loginform;

public class Test
{
	public static void main(String[] args)
	{
		for (String part : HTMLReader.readHtml("C:/Users/Nico/Desktop/test.html"))
			System.out.println(part);
	}
}
