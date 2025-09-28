package model;

public class Voucher {
    private static int codeCounter = 0;
    private int code;
    private double discountAmount;
    private boolean universal_voucher;
    private String remark;
    private boolean voucher_applied;
    private String expiry_date;

    public Voucher(double discountAmount, boolean universal_voucher, String remark, String expiry_date){
        this.code = ++codeCounter;
        this.discountAmount = discountAmount;
        this.universal_voucher = universal_voucher;
        this.remark = remark;
        this.voucher_applied = false;
        this.expiry_date = expiry_date;
    }

    public double getPrice() {
        return discountAmount;
    }
    public void setApplied(boolean applied){
        this.voucher_applied = applied;
    }
    
    public int getCode() { return code; }
    public boolean isUniversal() { return universal_voucher; }
    public String getRemark() { return remark; }
    public String getExpiryDate() { return expiry_date; }
    public boolean getVoucherApplied(){ return voucher_applied;}
}