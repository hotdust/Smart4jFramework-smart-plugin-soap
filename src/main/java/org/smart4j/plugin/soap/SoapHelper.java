package org.smart4j.plugin.soap;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;

/**
 * Created by shijiapeng on 17/1/9.
 */
public class SoapHelper {

    private static final List<Interceptor<? extends Message>> inInterceptorList =
            new ArrayList<>();
    private static final List<Interceptor<? extends Message>> outInterceptorList =
            new ArrayList<>();

    static {
        if (SoapConfig.isLog()) {

            LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
            inInterceptorList.add(loggingInInterceptor);

            LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
            outInterceptorList.add(loggingOutInterceptor);
        }
    }


    /**
     * 生成Soap服务
     * @param wsdl
     * @param interfaceClass
     * @param implementInstance
     */
    public static void publishService(String wsdl, Class<?> interfaceClass, Object implementInstance) {
        ServerFactoryBean factory = new ServerFactoryBean();
        factory.setAddress(wsdl);
        factory.setServiceClass(interfaceClass);
        factory.setServiceBean(implementInstance);
        factory.setInInterceptors(inInterceptorList);
        factory.setOutInterceptors(outInterceptorList);
        factory.create();
//        Server server = factory.create();
//        server.getEndpoint().getEndpointInfo().getAddress()
    }

    public static <T> T createClient(String wsdl, Class<? extends T> interfaceClass) {

        ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
        factory.setAddress(wsdl);
        factory.setServiceClass(interfaceClass);
        factory.setInInterceptors(inInterceptorList);
        factory.setOutInterceptors(inInterceptorList);
        return factory.create(interfaceClass);
    }




}
