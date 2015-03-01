package com.hashtag.service;

import java.util.List;

/**
 * The article representation for http response in json format.
 * 
 * @author guangfeng
 *
 */
public class JsonArticle {
	private String s;// url
	private String t;// title
	private String st; // subtitle
	private List<String> i;// image url list
	private List<String> a; // author list
	private List<String> p;// paragraphs
	private String T;// topic
	private String S;// subtopic
	private String RT; // raw topic
	private String RS; // raw subtopic
	private String TU; // raw topic from url
	private String SU; // raw subtopic from url
	private long D; // article date
	private long CD; // crawl date

	public String gets() {
		return s;
	}

	public void sets(String s) {
		this.s = s;
	}

	public String gett() {
		return t;
	}

	public void sett(String t) {
		this.t = t;
	}

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public List<String> getI() {
		return i;
	}

	public void setI(List<String> i) {
		this.i = i;
	}

	public List<String> getA() {
		return a;
	}

	public void setA(List<String> a) {
		this.a = a;
	}

	public List<String> getP() {
		return p;
	}

	public void setP(List<String> p) {
		this.p = p;
	}

	public String getT() {
		return T;
	}

	public void setT(String t) {
		T = t;
	}

	public String getS() {
		return S;
	}

	public void setS(String s) {
		S = s;
	}

	public String getRT() {
		return RT;
	}

	public void setRT(String rT) {
		RT = rT;
	}

	public String getRS() {
		return RS;
	}

	public void setRS(String rS) {
		RS = rS;
	}

	public String getTU() {
		return TU;
	}

	public void setTU(String tU) {
		TU = tU;
	}

	public String getSU() {
		return SU;
	}

	public void setSU(String sU) {
		SU = sU;
	}

	public long getD() {
		return D;
	}

	public void setD(long d) {
		D = d;
	}

	public long getCD() {
		return CD;
	}

	public void setCD(long cD) {
		CD = cD;
	}
}
