package xyz.raphaelscheinkoenig.pretty.util;

import java.util.ArrayList;
import java.util.List;

public class WeightedList<T> {

	private double currentWeight = 0;
	final List<WeightedItem<T>> list;
	
	public WeightedList(){
		list = new ArrayList<>();
	}
	
	public void add(double weight, T item){
		currentWeight += weight;
		list.add(new WeightedItem<T>(weight, item));
	}
	
	public T get(){
		double val = Math.random() * currentWeight;
		double temp = 0;
		for(WeightedItem<T> item : list){
			temp += item.weight;
			if(temp >= val){
				return item.item;
			}
		}
		return null;
	}
	
	public List<WeightedItem<T>> getItems(){
		List<WeightedItem<T>> copy = new ArrayList<>(list.size());
		for(WeightedItem<T> item : list){
			copy.add(item);
		}
		return copy;
	}
	
	public List<T> getContents(){
		List<T> copy = new ArrayList<>();
		for(WeightedItem<T> item : list){
			copy.add(item.item);
		}
		return copy;
	}
	
	public static class WeightedItem<T> {
		private double weight;
		private T item;
		private WeightedItem(double weight, T item){
			this.weight = weight;
			this.item = item;
		}
	}
	
}
