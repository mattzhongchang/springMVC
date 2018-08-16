package com.matt.business.good.entities;

import java.io.Serializable;


public class Goods implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 编号
	 */
	private int id;
	/*
	 * 名称
	 */
	private String name;
	/*
	 * 价格
	 */
	private double price;
	/*
	 * 图片
	 */
	private String picture;
	
	public Goods() {
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	@Override
	public String toString() {
		return "id："+getId()+"，name："+getName()+"，price："+getPrice()+"，picture："+getPicture();
	}

}
