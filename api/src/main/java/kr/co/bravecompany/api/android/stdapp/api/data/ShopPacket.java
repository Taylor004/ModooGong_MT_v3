package kr.co.bravecompany.api.android.stdapp.api.data;

public final class ShopPacket {

    public static class MyCouponInfo extends Packet.ResponseResult
    {
        public int count;
    }

    public static class NotificationInfo extends Packet.ResponseResult
    {
        public int count;
    }

    public static class MyCartInfo extends Packet.ResponseResult
    {
        public int count;
    }

    public static class MyCountInfo extends Packet.ResponseResult
    {
        public int [] count;

        public int getCount() {
            return (count!=null && count.length > 0) ? count[0] : 0;
        }
    }
}
