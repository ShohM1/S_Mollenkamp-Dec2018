package fracCalc;
import java.util.*;

public class FracCalc {

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        System.out.println("Enter a mathematical expression that you want to calculate (e.g. 1_2/3 + 4)");
        String input = console.nextLine();//take input
        String ans = "1";
        while(!input.equals("quit")) {
        	input = input.replaceAll("ans", ans);
        	ans = produceAnswer(input);//run multiple produce answers
        	System.out.println(ans);//print out the answer
        	System.out.println("Enter another mathematical expression or type \"quit\", if you want to quit.");
        	input = console.nextLine();//get the next input
        } 
        console.close();
    }
    
    public static String produceAnswer(String input) {
    	if(input.contains("/0")){
    		return "ERROR: Cannot divide by zero.";//check if dividing by 0
    	}
    	String[] separation = input.split(" ");//split between operands and operators
    	for(int i=0; i<(separation.length-1)/2; i++) {
    		int calcNow = 1;//what to calculate
    		//for(int j=1; j<separation.length; j+=2) {
    		//	if(separation[j].equals("/")||separation[j].equals("*")) {//if there's multiply or divide
    		//		calcNow = j;
    		//		j+=separation.length;//end the loop when found
    		//	}
    		//}
    		String checkValid = separation[calcNow].replace("-","+").replace("*","+").replace("/", "+");
    		for(int k=-1; k<=1; k+=2) {
    			String checkValid2 = separation[calcNow + k].replace("1","0").replace("2","0").replace("3","0").replace("4","0").replace("5","0").replace("6","0").replace("7","0").replace("8","0").replace("9","0").replace("/","").replace("_","").replace("-","");
    			for(int l=0; l<checkValid2.length();l++) {
    				if(checkValid2.charAt(l)!='0') {//checking if the input is just numbers,"-", "/", or "_"
    					checkValid = "notWork";
    				}
    			}
    			if(checkValid2.equals("")) {//checking if it's not only "-","/", or "_"
    				checkValid = "notWork";
    			}
    		}
    		if(!checkValid.equals("+")) {
    			return "ERROR: Input is in an invalid format.";//if it's not proper operator, error
    		}
    		String[] toCalculate = {separation[calcNow-1], separation[calcNow], separation[calcNow+1]};
    		if(toCalculate[1].equals("/")&&toCalculate[2].equals("0")) {
    			return "ERROR: Cannot divide by zero";
    		}
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
    	if(operator.equals("+")||operator.equals("-")) {//if it's + or -
    		if(operator.equals("-")) {
    			frac2[0] = -frac2[0];//make the second improper fraction negative if minus
    		}
    		answer[1] = frac1[1]*frac2[1];//denominator multiplied
    		answer[0] = frac1[0]*frac2[1] + frac2[0]*frac1[1];//add numerators multiplied by denom of other
    	}else if(operator.equals("*")||operator.equals("/")){//if it's * or /
    		if(operator.equals("/")) {
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
		num1= Math.abs(num1);
		num2= Math.abs(num2);
		int divisor=num2;
		if(num1<num2 && num1!=0) {
			divisor = num1;
		}
		while(num1%divisor!=0||num2%divisor!=0){
		divisor--;
		}
		return divisor;
    }
}