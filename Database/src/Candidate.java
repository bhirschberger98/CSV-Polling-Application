public class Candidate {
	private int votes;
	private String name;
	Candidate(String name){
		this.name=name;
		this.votes=1;
	}
	public void incrementVotes() {
		this.votes++;
	}
	public String getName() {
		return this.name;
	}
	public int getVotes() {
		return this.votes;
	}
	public void addVotes(int v) {
	    this.votes += v;
	}
	public String toString() {
		if(votes==1) {
			return this.name+" has "+this.votes+" vote.\n";
		}else {
			return this.name+" has "+this.votes+" votes.\n";
		}
	}
}