public class Candidate {
	private int votes;
	private String name;
	private String committee;
	Candidate(String name, String committee){
		this.name=name;
		this.committee=committee;
		this.votes=1;
	}
	public void incrementVotes() {
		this.votes++;
	}
	public String getName() {
		return this.name;
	}
	public String getCommittee() {
		return this.committee;
	}
	public int getVotes() {
		return this.votes;
	}
	public void addVotes(int v) {
	    this.votes += v;
	}
	public String toString() {
		if(votes==1) {
			return this.name+" is in the "+this.committee+" committee and has "+this.votes+" vote.\n";
		}else {
			return this.name+" is in the "+this.committee+" committee and has "+this.votes+" votes.\n";
		}
	}
}