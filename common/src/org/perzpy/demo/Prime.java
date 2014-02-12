package org.perzpy.demo;

/**
 * 素数算法
 * 
 * @author zpy
 * @date Feb 12, 2014
 */
public class Prime {

	public static void main(String[] args) {
		//100以内的素数
		for (int i = 2; i <= 100; i++) {
			if (isPrime(i)) {
				System.out.println(i);
			}
		}

	}

	static boolean isPrime(int num) {
		if (num <= 1) {
			return false;
		}

		for (int i = 2; i * i <= num; i++) {
			if (num % i == 0) {
				return false;
			}
		}
		return true;
	}
}
