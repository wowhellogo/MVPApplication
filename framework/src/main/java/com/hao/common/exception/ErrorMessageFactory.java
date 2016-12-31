package com.hao.common.exception;

import android.content.Context;
import android.content.res.Resources;

import com.hao.common.R;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @Package com.daoda.aijiacommunity.common.exception
 * @作 用:异常处理
 * @创 建 人: linguoding
 * @日 期: 2016-01-15
 */
public class ErrorMessageFactory {

    private ErrorMessageFactory() {
        //empty
    }

    public static String create(Context context, Exception exception) {
        String message = context.getString(R.string.exception_message_generic);

        if (exception instanceof UnknownHostException) {
            message = context.getString(R.string.exception_message_no_connection);
        } else if (exception instanceof Resources.NotFoundException) {
            message = context.getString(R.string.exception_message_user_not_found);
        } else if (exception instanceof SocketTimeoutException) {
            message = context.getString(R.string.exception_message_time_out);
        } else if (exception instanceof NotFoundDataException) {
            message = exception.getMessage() != null ? exception.getMessage()
                    : context.getString(R.string.exception_message_not_data);
        } else if (exception instanceof RestErrorException) {
            message = exception.getMessage() != null ? exception.getMessage()
                    : context.getString(R.string.exception_message_rest_error);
        } else if (exception instanceof ResolveErrorException) {
            message = context.getString(R.string.exception_message_resolve_error);
        }else if(exception instanceof NotDataListException){
            message=exception.getMessage()!=null?exception.getMessage()
                    :context.getString(R.string.exception_message_not_data);
        }else{
            message=exception.getMessage()!=null?exception.getMessage():message;
        }
        return message;
    }
}
