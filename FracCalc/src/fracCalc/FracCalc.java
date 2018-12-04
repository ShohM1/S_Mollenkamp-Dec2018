package fracCalc;
import java.util.*;

public class FracCalc {

    public static void main(String[] args) 
    {
        Scanner console = new Scanner(System.in);
        System.out.println("Enter a mathematical expression that you want to calculate (e.g. 1_2/3 + 4)");
        String input = console.nextLine();//take input
        while(input!="quit") {
        	System.out.println(produceAnswer(input));//run multiple produce answers and print out
        	System.out.println("Enter another mathematical expression or type \"quit\", if you want to quit.");
        	input = console.nextLine();//get the next input
        } 
        console.close();
    }
    
    // ** IMPORTANT ** DO NOT DELETE THIS FUNCTION.  This function will be used to test your code
    // This function takes a String 'input' and produces the result
    //
    // input is a fraction string that needs to be evaluated.  For your program, this will be the user input.
    //      e.g. input ==> "1/2 + 3/4"
    //        
    // The function should return the result of the fraction after it has been calculated
    //      e.g. return ==> "1_1/4"
    public static String produceAnswer(String input) {
    	String[] separation = input.split(" ");//split between operands and operators
    	
    	for(int i=0; i<(separation.length-1)/2; i++) {
    		int calcNow = 1;//what to calculate
    		//for(int j=1; j<separation.length; j+=2) {
    			
    			//if(separation[j].equals("/")||separation[j].equals("*")) {//if there's multiply or divide
    				//calcNow = j;
    				//j+=separation.length;//end the loop when found
    			//}
    		//}
    		String[] toCalculate = {separation[calcNow-1], separation[calcNow], separation[calcNow+1]};
    		String answer1 = eachCalc(toCalculate);//answer of the first one
    		for(int k=0; k<separation.length; k++) {//move around the values in the array
    			separation[k] = separation[k];
    			if(k==calcNow-1) {
    				separation[k] = answer1;
    			}else if(separation.length-k<3) {
    				separation[k] = "";
    			}else if(k-calcNow>=0) {
    				separation[k] = separation[k+2];
    			}
    		}
    	}
    	return separation[0];
    }
    public static String eachCalc(String[] input)
    { 
        // TODO: Implement this function to produce the solution to the input
        int[] splac1 = inToFrac(input[0]);//improper fraction of the first operand
        int[] splac2 = inToFrac(input[2]);//improper fraction of the second operand
        int[] calculated = operate(splac1, splac2, input[1]);//get the answer from operating two improper fractions
        String answer = calculated[0]/calculated[1] + "_" + Math.abs(calculated[0]%calculated[1]) +"/" + calculated[1];//back into string with mixed
        if(calculated[0]%calculated[1]==0) {
        	answer = calculated[0]/calculated[1]+"";//take out fraction if answer is whole
        } else if(calculated[0]/calculated[1]==0) {
        	answer = calculated[0]%calculated[1] + "/" + calculated[1];//take out 0_
        }
        return answer;
    }

    // TODO: Fill in the space below with any helper methods that you think you will need
    public static int[] inToFrac(String operand) {
    	int slack = operand.indexOf("/");//checking for fraction
    	int undack = operand.indexOf("_");//checking for whole
    	if(slack==-1) {
    		operand = operand + "_0/1";
    	} else if(undack==-1) {
    		operand = "0_" + operand;
    	}
    	operand = operand.replace("_"," ").replace("/"," ");//make it splittable
    	String[] splitFrac = operand.split(" ");//split into fractions
    	int whole = 0;//set whole number
    	int[] impropFrac = new int[2];
    	for(int i=0; i<=2; i++) {
    		int inInt = Integer.parseInt(splitFrac[i]);//making string into int
    		if(i==0) {
    			whole = inInt;//store whole number
    		} else {
    			impropFrac[i-1] = inInt;//store fractions
    		}
    	}
    	if(impropFrac[0]<0 && whole!=0) {
    		impropFrac[0]=-impropFrac[0];
    		whole = -whole;
    	}
    	if(whole>0) {
    		impropFrac[0]+=whole*impropFrac[1];//make it improper
    	}else if(whole<0) {
    		impropFrac[0]= -impropFrac[0]+whole*impropFrac[1];//make it improper when negative
    	}
    	return impropFrac;
    }
    
    
    public static int[] operate(int[] frac1, int[] frac2, String operator) {
    	int[] answer = new int[2];
    	if(operator.equals("+")==true||operator.equals("-")==true) {//if it's + or -
    		if(operator.equals("-")==true) {
    			frac2[0] = -frac2[0];//make the second improper fraction negative if minus
    		}
    		answer[1] = frac1[1]*frac2[1];//denominator multiplied
    		answer[0] = frac1[0]*frac2[1] + frac2[0]*frac1[1];//add numerators multiplied by denom of other
    	}else if(operator.equals("*")==true||operator.equals("/")==true){//if it's * or /
    		if(operator.equals("/")==true) {
    			int temp = frac2[1];
    			frac2[1] = frac2[0];
    			frac2[0] = temp;//switch numerator and denominator
    		}
    		answer[0] = frac1[0]*frac2[0];//multiply numerator
    		answer[1] = frac1[1]*frac2[1];//multiply denominator
    	}
    	if(answer[1]<0) {
    		answer[1]=-answer[1];
    		answer[0]=-answer[0];//if negative on denominator, move it to numerator
    	}
    	int greatestCF = gcf(answer[0],answer[1]);
    	answer[0]/=greatestCF;
    	answer[1]/=greatestCF;
    	return answer;
    }
    public static int gcf(int num1, int num2) {
		//finds greatest common factor by checking the divisibility of both values
		num1= (int) Math.abs(num1);
		num2= (int) Math.abs(num2);
		int divisor=num1;
		if(num1==0) {
			divisor=num2;
		}else{
			while(num1%divisor!=0||num2%divisor!=0){
			divisor--;
			}
		}
		return divisor;
    }
}