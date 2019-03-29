


enum seamShape  //there is two ways which we want to compare
{ 
	straight,general; 
} 
public class seamCalculate {
	Coordinates[] coors; // the seam coordinates
	seamShape shape; // which way we want to calculate
	
	seamCalculate(EnergyFunctions energy,seamShape shape){
		this.shape=shape;
		this.coors=new Coordinates[energy.rows];
	}
	
	void getSeam(){
		if(this.shape==seamShape.straight)
			straightCalculation();
		else{
			generalCalculation();
		}
	}

	private void generalCalculation() {
		// TODO Auto-generated method stub
		
	}

	private void straightCalculation() {
		// TODO Auto-generated method stub
		
	}
	
	
}

class Coordinates{
	int row=-1;
	int col=-1;
	
	Coordinates(int m,int n){
		this.row=m;
		this.col=n;
	}
}