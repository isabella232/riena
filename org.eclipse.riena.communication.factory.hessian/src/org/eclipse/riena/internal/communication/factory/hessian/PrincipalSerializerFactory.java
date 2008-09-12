package org.eclipse.riena.internal.communication.factory.hessian;

import java.security.Principal;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.Serializer;

public class PrincipalSerializerFactory extends AbstractSerializerFactory {

	@Override
	public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
		if (cl.isInterface() && (!cl.getPackage().getName().startsWith("java") || cl == Principal.class)) { //$NON-NLS-1$
			return new JavaDeserializer(cl);
		}
		return null;
	}

	@Override
	public Serializer getSerializer(Class cl) throws HessianProtocolException {
		return null;
	}

}
