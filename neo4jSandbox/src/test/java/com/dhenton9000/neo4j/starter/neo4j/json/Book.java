package com.dhenton9000.neo4j.starter.neo4j.json;

import java.util.List;

public class Book {
	
	private String title = "Gone with the Wind";
	private String author = "Margaret Mitchell";
	private String iSBN = null;
	private List<String> reviewers = null;
	private int numPages = 0;
	private String officeName = null;
	
	public Book(String newTitle, String newAuthor)
	{
		setTitle(newTitle);
		setAuthor(newAuthor);
	}
	
	public Book()
	{
		
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public void setReviewers(List<String> reviewers) {
		this.reviewers = reviewers;
	}

	public List<String> getReviewers() {
		return reviewers;
	}

	
	public void setNumPages(int numPages) {
		this.numPages = numPages;
	}

	public int getNumPages() {
		return numPages;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setiSBN(String iSBN) {
		this.iSBN = iSBN;
	}

	public String getiSBN() {
		return iSBN;
	}

	 
	
}
