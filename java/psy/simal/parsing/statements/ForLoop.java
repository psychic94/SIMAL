package psy.simal.parsing.statements;

import psy.simal.parsing.Block;
import psy.simal.parsing.CodePart;

public class ForLoop extends Block{
	private final int index;
	private final String varName;
	private final int interval;
	private final Range range;
	public ForLoop(int index, String varName, int interval, Range range){
		this.index = index;
		this.varName = varName;
		this.interval = interval;
		this.range = range;
	}

	@Override
	public void run() {
		//TODO: use the variable cache to store the pointer
		int current = range.getStart();
		do{
			for(CodePart part : code){
				part.run();
			}
			current += interval;
		}while(range.includes(current));
	}

	public class Range{
		private final int start, end;
		private final boolean inclusive;
		public Range(int start, int end, boolean inclusive){
			this.start = start;
			this.end = end;
			this.inclusive = inclusive;
		}
		
		public int getStart(){
			return start;
		}
		
		public int getEnd(){
			return end;
		}

		public boolean isInclusive(){
			return inclusive;
		}
		
		public boolean includes(int value){
			if(inclusive && start < end)
				return (value >= start && value <= end);
			else if(inclusive && start > end)
				return (value <= start && value >= end);
			else if(!inclusive && start < end)
				return (value >= start && value < end);
			else if(!inclusive && start > end)
				return (value <= start && value > end);
			else
				return (value == start);
		}
		
		public String getCompOper(){
			if(inclusive && start < end)
				return "<=";
			else if(inclusive && start > end)
				return ">=";
			else if(!inclusive && start < end)
				return "<";
			else
				return ">";
		}
	}
	
	public void debug(){
		String header = "for(double ";
		header += varName + "=" + range.getStart() + "; ";
		header += varName + range.getCompOper() + range.getEnd() + "; ";
		header += varName + "+=" + interval + "){";
		System.out.println(header);
		for(CodePart part : code){
			part.debug();
		}
		System.out.println("}");
	}
}
