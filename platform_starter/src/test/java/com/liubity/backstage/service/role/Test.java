package com.liubity.backstage.service.role;

/**
 * @Author Liubity
 * @Date 2021-07-30
 */
public class Test {

    private String name;
    
    public Test(String name){
        this.name = name;
        
    }
    
    
    public static void main(String[] args) {
        Test t = new Test("ABC");
        System.out.println(t.name);
        
        
    }
    
}
