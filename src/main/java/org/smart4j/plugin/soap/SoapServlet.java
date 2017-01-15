package org.smart4j.plugin.soap;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ClassHelper;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import java.util.Set;

/**
 * Created by shijiapeng on 17/1/9.
 */
@WebServlet(urlPatterns = SoapConstant.SERVLET_URL, loadOnStartup = 0)
public class SoapServlet extends CXFNonSpringServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoapServlet.class);

    @Override
    protected void loadBus(ServletConfig sc) {
        super.loadBus(sc);
        Bus bus = getBus();
        BusFactory.setDefaultBus(bus);
        publishSoapService();
    }

    private void publishSoapService() {
        Set<Class<?>> soapClassSet = ClassHelper.getClassSetByAnnotation(Soap.class);

        if (CollectionUtil.isNotEmpty(soapClassSet)) {
            for (Class<?> soapClass : soapClassSet) {
                LOGGER.info("Soap class: " + soapClass.getSimpleName());

                // 获取Soap地址
                String address = getAddress(soapClass);
                // 获取Soap接口
                Class<?> soapInterfaceClass = getSoapInterfaceClass(soapClass);
                // 获取Soap实例
                Object soapInstance = BeanHelper.getBean(soapClass);
                // 发布Soap服务
                // TODO: 17/1/10 为什么在使用这个Servlet里发布时，address就不使用全路径"http://www.project.com/soap/testsoap"
                // TODO: 17/1/10 使用"/testsoap"就行，Bus里做什么了吗？
                // TODO: 17/1/10 CXFNonSpringServlet的例子也是这么写的。http://cxf.apache.org/docs/servlet-transport.html
                SoapHelper.publishService(address, soapInterfaceClass, soapInstance);
            }
        }
    }

    private String getAddress(Class<?> soapClass) {
        String address;
        String soapValue = soapClass.getAnnotation(Soap.class).value();
        if (StringUtil.isNotEmpty(soapValue)) {
            address = soapValue;
        } else {
            address = getSoapInterfaceClass(soapClass).getSimpleName();
        }

        if (!address.startsWith("/")) {
            address = "/" + address;
        }

        // 把多个"/"(例如：///)变成一个"/"
        address = address.replaceAll("\\/+", "/");
        return address;
    }

    private Class<?> getSoapInterfaceClass(Class<?> soapClass) {

        Class<?> soapInterface = soapClass.getInterfaces()[0];
        return soapInterface;
    }
}
