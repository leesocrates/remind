package com.lee.library.network;

import com.google.gson.Gson;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * Created by lee on 2016/1/18.
 */
public class SpecialConverterFactory extends Converter.Factory {
    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static SpecialConverterFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON andA
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static SpecialConverterFactory create(Gson gson) {
        return new SpecialConverterFactory(gson);
    }

    private final Gson gson;

    private SpecialConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    static final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final Type type;

        GsonResponseBodyConverter(Gson gson, Type type) {
            this.gson = gson;
            this.type = type;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            Reader reader = value.charStream();
            String responseStr = readerToString(reader);
            try {
                return gson.fromJson(responseStr, type);
//                return gson.fromJson(reader, type);
            } finally {
                closeQuietly(reader);
            }
        }
    }

    static final class SpecialRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private static final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;
        private final Type type;

        SpecialRequestBodyConverter(Gson gson, Type type) {
            this.gson = gson;
            this.type = type;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            if (value instanceof byte[]) {
                return RequestBody.create(MEDIA_TYPE, (byte[]) value);
            }
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            try {
                gson.toJson(value, type, writer);
                writer.flush();
            } catch (IOException e) {
                throw new AssertionError(e); // Writing to Buffer does no I/O.
            }
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }

    static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }

    private static String readerToString(Reader reader)
            throws IOException {
        char[] arr = new char[8 * 1024];
        StringBuilder buffer = new StringBuilder();
        int numCharsRead;
        while ((numCharsRead = reader.read(arr, 0, arr.length)) != -1) {
            buffer.append(arr, 0, numCharsRead);
        }
        reader.close();
        String targetString = buffer.toString();
        return targetString;
    }
}

