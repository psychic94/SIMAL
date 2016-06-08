package psy.simal.compiling;

public abstract class SimalCompiler{
	public static void compile(String[] lines, String filename){
	}
	
	public static byte[] floatToBytes(float value){
		int bits = Float.floatToIntBits(value);
		return intToBytes(bits);
	}
	
	public static byte[] intToBytes(int value){
		byte[] bytes = {
			(byte)(value&0xFF),
			(byte)((value&0xFF00)>>8),
			(byte)((value&0xFF0000)>>16),
			(byte)((value&0xFF000000)>>24),
		};
		return bytes;
	}
}
