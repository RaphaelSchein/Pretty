package xyz.raphaelscheinkoenig.pretty.commands;

public class ArgumentParser {
	
	private String[] args;
	
	public ArgumentParser(String[] args) {
		this.args = args;
	}
	
	public static ArgumentParser parse(String[] args){
		return new ArgumentParser(args);
	}
	
	public int size(){
		return args.length;
	}

	public String get(int index){
		return args[index-1];
	}
	
	public Double getDouble(int index){
		try {
			Double d = Double.parseDouble(get(index));
			return d;
		} catch(Exception ex){
			return null;
		}
	}
	
	public Integer getInt(int index){
		try {
			Integer i = Integer.parseInt(get(index));
			return i;
		} catch(Exception ex){
			return null;
		}
	}
	
	public Boolean getBoolean(int index){
		try {
			Boolean b = Boolean.parseBoolean(get(index));
			return b;
		} catch(Exception ex){
			return null;
		}
	}
	
	public boolean hasNoArguments(){
		return size() == 0;
	}
	
	public boolean hasExactly(int arguments){
		return size() == arguments;
	}
	
	public boolean hasNotExactly(int arguments){
		return !hasExactly(arguments);
	}
	
	public boolean hasAtLeast(int arguments){
		return size() >= arguments;
	}
	
	public boolean hasLessThan(int arguments){
		return size() < arguments;
	}

}
