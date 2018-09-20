package com.org.util;

import java.util.Scanner;

public class NumberToWordConverter {
	
	  public String pw(int n,String ch)
	{
		String result ="";
		String one[] = { " ", " one", " two", " three", " four", " five", " six", " seven", " eight", " nine", " ten",
				" eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen", " seventeen", " eighteen",
				" nineteen" };

		String ten[] = { " ", " ", " twenty", " thirty", " forty", " fifty", " sixty", " seventy", " eighty",
				" ninety" };

		if (n > 19) {
			System.out.print(ten[n / 10] + "" + one[n % 10]);
			result+=ten[n / 10] + "" + one[n % 10];
		} else {
			System.out.print(one[n]);
			result+=one[n];
		}
		if (n > 0) {
			System.out.print(ch);
			result+=ch;
		}
		return result;
	}
	  
	public static String convert(int n) {
		String word = "";
		if (n <= 0) {
		} else {
			NumberToWordConverter a = new NumberToWordConverter();
			word += a.pw((n / 1000000000), " hundred");
			word += a.pw((n / 10000000) % 100, " crore");
			word += a.pw(((n / 100000) % 100), " lakh");
			word += a.pw(((n / 1000) % 100), " thousand");
			word += a.pw(((n / 100) % 10), " hundred");
			word += a.pw((n % 100), " ");
		}
		System.out.println("-----");
		return word;
	}

	public static void main(String[] args) {
		 int n=0;
		    Scanner scanf = new Scanner(System.in);
		    System.out.println("Enter an integer number: ");
		    n = scanf.nextInt();
		    
		    System.out.println(convert(n));

	}

}
