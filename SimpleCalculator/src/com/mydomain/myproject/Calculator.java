/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mydomain.myproject;

import java.util.ArrayList;
import java.util.StringTokenizer;
/**
 *
 * @author nathan
 */
public final class Calculator {
    
    public static final String INVALID = "Invalid Expression.";
    
    public static String calculate(String expr){
        /*if (!expr.matches("((\\d+\\D)*\\d+)+")) {
            return INVALID;
        }*/
        ArrayList<String> operands = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(expr, "+-/*^()", true);
        while (st.hasMoreTokens()) {
            operands.add(st.nextToken());
        }
        if (isValid(operands)){
            return expand(operands);
        } else {
            return INVALID;
        }
    }
    
    private static boolean isValid(ArrayList<String> operands){
        if (parentheseCheck(0,operands, 0,false,false) > -1){
            return true;
        }
        return false;
    }
    
    private static int parentheseCheck(int depth,ArrayList<String> operands, int startIndex, boolean searching,boolean prevSearching){
        int match = 0;
        for (int i = startIndex; i < operands.size() && match > -1; i++){
            //System.out.println(""+i+","+depth+","+searching+","+prevSearching);
            String token = operands.get(i);
            if (token.equals("(") && !searching){
                searching = true;
            } else if (token.equals("(") && searching){
                match = parentheseCheck(depth+1,operands, i+1, true, true);
                searching = false;
                i = match;                
            }
            
            if (token.equals(")") && !searching && prevSearching){
                return i;
            }
            if (token.equals(")") && !searching && !prevSearching){    
                return -2;
            }
            if (token.equals(")") && searching){       
                searching = false;
            } 
        }
        if (searching || prevSearching){
            return -3;
        }
        return match;
    }
    
    private static String expand(ArrayList<String> operands){ 
        int left = 0;
        int right = 0;
        for (int i = 0; i < operands.size(); i++){
            if (operands.get(i).equals("(")){
                left = i;
            }
            if (operands.get(i).equals(")")){       
                right = i;
                operands.remove(right);
                operands.remove(left);
                operands = evaluate(left,right-2,operands);
                if (operands.get(0).equals(INVALID)){
                    return INVALID;
                }
                left = 0;
                i = 0;
            }
        }
        return evaluate(0,operands.size()-1, operands).get(0);
    }
    
    private static ArrayList<String> evaluate(int startIndex, int endIndex, ArrayList<String> operands) { 
       String expression = "";
        for (int k = startIndex; k <= endIndex && k < operands.size(); k++){
            expression += operands.get(k);          
        }
        //System.out.println(expression);
        if (!expression.matches("((\\d+\\D)*\\d+)")) {
            ArrayList<String> invalid = new ArrayList<String>();
            invalid.add(INVALID);
            return invalid;
        }        
        for (int i = startIndex; i < endIndex && operands.size() > 1; i++){
            if (operands.get(i).equals("^")){           
                double x = Math.pow(Integer.parseInt(operands.get(i-1)), Integer.parseInt(operands.get(i+1)));
                operands.set(i, Integer.toString((int) x));
                operands.remove(i-1);
                operands.remove(i);
                endIndex -= 2;
                i--;
            }
        }
 
        for (int i = startIndex; i < endIndex && operands.size() > 1; i++){
            if (operands.get(i).equals("*")){
                int x = Integer.parseInt(operands.get(i-1)) * Integer.parseInt(operands.get(i+1));
                operands.set(i, Integer.toString(x));
                operands.remove(i-1);
                operands.remove(i);
                endIndex -= 2;
                i--;
            } else if (operands.get(i).equals("/")){
                int x = Integer.parseInt(operands.get(i-1)) / Integer.parseInt(operands.get(i+1));
                operands.set(i, Integer.toString(x));
                operands.remove(i-1);
                operands.remove(i);
                endIndex -= 2;
                i--;
            }          
        }

        for (int i = startIndex; i < endIndex && operands.size() > 1 && i < operands.size(); i++){
            if (operands.get(i).equals("+")){
                int x = Integer.parseInt(operands.get(i-1)) + Integer.parseInt(operands.get(i+1));
                operands.set(i, Integer.toString(x));
                operands.remove(i-1);
                operands.remove(i);
                endIndex -= 2;
                i--;
            } else if (operands.get(i).equals("-")){
                int x = Integer.parseInt(operands.get(i-1)) - Integer.parseInt(operands.get(i+1));
                operands.set(i, Integer.toString(x));
                operands.remove(i-1);
                operands.remove(i);
                endIndex -= 2;
                i--;
            }     
        }

        return operands;
    }
}
