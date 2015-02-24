package rpc.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Omar MEBARKI on 22/02/15.
 */
public class Util {
    public static String computeMethodHash(Method var0) {
        long var1 = 0L;
        ByteArrayOutputStream var3 = new ByteArrayOutputStream(127);

        try {
            MessageDigest var4 = MessageDigest.getInstance("SHA");
            DataOutputStream var5 = new DataOutputStream(new DigestOutputStream(var3, var4));
            String var6 = getMethodNameAndDescriptor(var0);


            var5.writeUTF(var6);
            var5.flush();
            byte[] var7 = var4.digest();

            for (int var8 = 0; var8 < Math.min(8, var7.length); ++var8) {
                var1 += (long) (var7[var8] & 255) << var8 * 8;
            }
        } catch (IOException var9) {
            var1 = -1L;
        } catch (NoSuchAlgorithmException var10) {
            throw new SecurityException(var10.getMessage());
        }

        return  Long.toString(var1);
    }

    private static String getMethodNameAndDescriptor(Method var0) {
        StringBuffer var1 = new StringBuffer(var0.getName());
        var1.append('(');
        Class[] var2 = var0.getParameterTypes();

        for (int var3 = 0; var3 < var2.length; ++var3) {
            var1.append(getTypeDescriptor(var2[var3]));
        }

        var1.append(')');
        Class var4 = var0.getReturnType();
        if (var4 == Void.TYPE) {
            var1.append('V');
        } else {
            var1.append(getTypeDescriptor(var4));
        }

        return var1.toString();
    }

    private static String getTypeDescriptor(Class<?> var0) {
        if (var0.isPrimitive()) {
            if (var0 == Integer.TYPE) {
                return "I";
            } else if (var0 == Boolean.TYPE) {
                return "Z";
            } else if (var0 == Byte.TYPE) {
                return "B";
            } else if (var0 == Character.TYPE) {
                return "C";
            } else if (var0 == Short.TYPE) {
                return "S";
            } else if (var0 == Long.TYPE) {
                return "J";
            } else if (var0 == Float.TYPE) {
                return "F";
            } else if (var0 == Double.TYPE) {
                return "D";
            } else if (var0 == Void.TYPE) {
                return "V";
            } else {
                throw new Error("unrecognized primitive type: " + var0);
            }
        } else {
            return var0.isArray() ? var0.getName().replace('.', '/') : "L" + var0.getName().replace('.', '/') + ";";
        }
    }

}
