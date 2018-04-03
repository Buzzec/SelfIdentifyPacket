package net.buzzec.selfIdentifyPacket;

/**
 * This is an example of a SelfIdentify enum that is also used for error returns.
 *
 * @author Buzzec
 */
@SuppressWarnings("unused")
public enum SelfIdentifyBasic implements ISelfIdentifyObjectEnum{
    UNUSED;

    @Override
    public byte getId(){
        return SelfIdentifyBaseIDs.SELF_IDENTIFY_BASIC_ID;
    }

    @Override
    public Class getObjectClass(){
        return null;
    }

    @Override
    public Object getFromBytes(byte[] bytes){
        return null;
    }

    @Override
    public byte[] getBytes(Object object){
        return new byte[0];
    }
}
