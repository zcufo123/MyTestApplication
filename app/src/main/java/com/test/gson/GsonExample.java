package com.test.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class GsonExample {

    public static void runPrimitives() {
        // Serialization
        Gson gson = new Gson();
        String temp;
        temp = gson.toJson(1);            // ==> 1
        temp = gson.toJson("abcd");       // ==> "abcd"
        temp = gson.toJson(new Long(10)); // ==> 10
        int[] values = {1};
        temp = gson.toJson(values);       // ==> [1]

        // Deserialization
        int one = gson.fromJson("1", int.class);
        Integer oneInteger = gson.fromJson("1", Integer.class);
        Long oneLong = gson.fromJson("1", Long.class);
        Boolean aBoolean = gson.fromJson("false", Boolean.class);
        String str = gson.fromJson("\"abc\"", String.class);
        String[] anotherStr = gson.fromJson("[\"abc\"]", String[].class);
    }

    public static void runArray() {
        Gson gson = new Gson();
        String temp;
        int[] ints = {1, 2, 3, 4, 5};
        String[] strings = {"abc", "def", "ghi"};

        // Serialization
        temp = gson.toJson(ints);     // ==> [1,2,3,4,5]
        temp = gson.toJson(strings);  // ==> ["abc", "def", "ghi"]

        // Deserialization
        int[] ints2 = gson.fromJson("[1,2,3,4,5]", int[].class);
        // ==> ints2 will be same as ints
    }

    public static void runCollections() {
        Gson gson = new Gson();
        Collection<Integer> ints = Arrays.asList(1, 2, 3, 4, 5);

        // Serialization
        String json = gson.toJson(ints);  // ==> json is [1,2,3,4,5]

        // Deserialization
        Type collectionType = new TypeToken<Collection<Integer>>() {
        }.getType();
        Collection<Integer> ints2 = gson.fromJson(json, collectionType);
        // ==> ints2 is same as ints
    }

    public static void runBagOfPrimitives() {
        class BagOfPrimitives {
            private int value1 = 1;
            private String value2 = "abc";
            private transient int value3 = 3;

            BagOfPrimitives() {
                // no-args constructor
            }
        }

        // Serialization
        BagOfPrimitives obj = new BagOfPrimitives();
        Gson gson = new Gson();
        String json = gson.toJson(obj);

        // ==> json is {"value1":1,"value2":"abc"}

        // Deserialization
        BagOfPrimitives obj2 = gson.fromJson(json, BagOfPrimitives.class);
        // ==> obj2 is just like obj
    }

    public static void runGeneric() {
        class Foo<T> {
            T value;
        }
        class Bar {
        }
        Gson gson = new Gson();
        Foo<Bar> foo = new Foo<Bar>();
        String json = gson.toJson(foo); // May not serialize foo.value correctly

        Type fooType = new TypeToken<Foo<Bar>>() {
        }.getType();
        gson.toJson(foo, fooType);

        Foo<Bar> foo1 = gson.fromJson(json, fooType);
    }

    public static void runRawCollections() {
        class Event {
            private String name;
            private String source;

            private Event(String name, String source) {
                this.name = name;
                this.source = source;
            }

            @Override
            public String toString() {
                return String.format("(name=%s, source=%s)", name, source);
            }
        }
        Gson gson = new Gson();
        Collection collection = new ArrayList();
        collection.add("hello");
        collection.add(5);
        collection.add(new Event("GREETINGS", "guest"));
        String json = gson.toJson(collection);
        System.out.println("Using Gson.toJson() on a raw collection: " + json);
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        String message = gson.fromJson(array.get(0), String.class);
        int number = gson.fromJson(array.get(1), int.class);
        Event event = gson.fromJson(array.get(2), Event.class);
        System.out.printf("Using Gson.fromJson() to get: %s, %d, %s", message, number, event);
    }

    public static void runNull() {
        class Foo {
            private final String s;
            private final int i;

            public Foo() {
                this(null, 5);
            }

            public Foo(String s, int i) {
                this.s = s;
                this.i = i;
            }
        }

        Gson gson = new GsonBuilder().serializeNulls().create();
        Foo foo = new Foo();
        String json = gson.toJson(foo);
        System.out.println(json);

        json = gson.toJson(null);
        System.out.println(json);
    }

    public static void runVersion() {
        class VersionedClass {
            @Since(1.1)
            private final String newerField;
            @Since(1.0)
            private final String newField;
            private final String field;

            public VersionedClass() {
                this.newerField = "newer";
                this.newField = "new";
                this.field = "old";
            }
        }
        VersionedClass versionedObject = new VersionedClass();
        Gson gson = new GsonBuilder().setVersion(1.0).create();
        String jsonOutput = gson.toJson(versionedObject);
        System.out.println(jsonOutput);
        System.out.println();

        gson = new Gson();
        jsonOutput = gson.toJson(versionedObject);
        System.out.println(jsonOutput);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface Foo {
        // Field tag only annotation
    }

    public static void runExpose() {

        class SampleObjectForTest {
            @Foo
            private final int annotatedField;
            private final String stringField;
            private final long longField;

            public SampleObjectForTest() {
                annotatedField = 5;
                stringField = "someDefaultValue";
                longField = 1234;
            }
        }

        class MyExclusionStrategy implements ExclusionStrategy {
            private final Class<?> typeToSkip;

            private MyExclusionStrategy(Class<?> typeToSkip) {
                this.typeToSkip = typeToSkip;
            }

            public boolean shouldSkipClass(Class<?> clazz) {
                return (clazz == typeToSkip);
            }

            public boolean shouldSkipField(FieldAttributes f) {
                return f.getAnnotation(Foo.class) != null;
            }
        }

        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new MyExclusionStrategy(String.class))
                .serializeNulls()
                .create();
        SampleObjectForTest src = new SampleObjectForTest();
        String json = gson.toJson(src);
        System.out.println(json);
    }

    public static void runNaming() {
        class SomeObject {
            @SerializedName("custom_naming") private final String someField;
            private final String someOtherField;

            public SomeObject(String a, String b) {
                this.someField = a;
                this.someOtherField = b;
            }
        }

        SomeObject someObject = new SomeObject("first", "second");
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        String jsonRepresentation = gson.toJson(someObject);
        System.out.println(jsonRepresentation);
    }
}
