package com.pisethjava.chain.demo;

public final class ChainCourseDemo {

    private ChainCourseDemo() {
    }

    public static void main(String[] args) {
        PaymentChainDemo.run();
        PaymentChainFailureDemo.run();
        ExpenseApprovalDemo.run();
        DynamicPaymentChainDemo.run();
        RoomPublishingHomeworkDemo.run();
        CollectAllValidationDemo.run();
    }
}
