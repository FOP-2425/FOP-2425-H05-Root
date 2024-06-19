package h05;

public class Stack {
    private final java.util.Stack<Integer> stack = new java.util.Stack<Integer>();
    private int sum = 0;

    public boolean empty(){
        return stack.empty();
    }

    public void push(int value){
        sum += value;
        stack.push(value);
    }

    public int pop(){
        int removedValue = stack.pop();
        sum -= removedValue;
        return removedValue;
    }

    public int getSum(){
        return sum;
    }

}
