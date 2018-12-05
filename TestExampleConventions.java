
class testExampleConventions {

	static final int Const = 7, MIN= 0;
	
	static int ReturnConstant() {
		return Const;
	}
	
	static int _sum_(int[] VECTOR) {
		int S = 0;
		for(int i = 0; i < VECTOR.length; i++)
			S += VECTOR[i];
		
		return S;
	}
	
	static int sumRange(int min, int max, int[] v) {
		int s = 0;
		while(min <= max) {
			s += v[min];
			min++;
		}
		return s;
	}
	
	
}
