

package com.freecat.container;


import com.freecat.http.HttpRequest;
import com.freecat.http.HttpResponse;

public interface Mapper {


    Container getContainer();


    void setContainer(Container container);

    /**
     * 映射规则
     * @param request
     * @param update
     * @return
     */
    Container map(HttpRequest request, boolean update);


}
