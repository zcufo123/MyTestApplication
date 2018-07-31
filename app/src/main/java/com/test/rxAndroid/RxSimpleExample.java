package com.test.rxAndroid;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class RxSimpleExample {

    private static final String TAG = "RxSimpleExample";

    public static void run() {
        runSimpleExample();
    }

    private static class Person {
        public int age;
        public String name;

        public Person() {

        }

        public Person(int age, String name) {
            this.age = age;
            this.name = name;
        }

        @Override
        public String toString() {
            return "name: " + name + " age: " + age;
        }
    }

    private static void runSimpleExample() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("first");
                Log.d(TAG, "subscribe(),first");
                e.onNext("second");
                Log.d(TAG, "subscribe(),second");
                e.onNext("third");
                Log.d(TAG, "subscribe(),third");
                e.onComplete();
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe()");
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.d(TAG, "onNext()," + s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError(),");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete(),");
                    }
                });

        Observable.just("1", "2", "3")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.e(TAG, "accept : onNext : " + s + "\n");
                    }
                });
    }

    private static void runSingle() {
        Single.just(1)// 只接收一个参数
                .subscribe(new SingleObserver<Integer>() {// SingleObserver是Single特定的观察者
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe()");
                    }

                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        Log.d(TAG, "onSuccess(),integer: " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError()");
                    }
                });
    }

    private static void runZip() {
        // 第一个事件
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(21);
                Log.d(TAG, "onNext(),21");
                e.onNext(22);
                Log.d(TAG, "onNext(),22");
                e.onNext(23);
                Log.d(TAG, "onNext(),23");
                e.onComplete();
                Log.d(TAG, "onComplete(),observable1");
            }
        });

        // 第二个事件
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("first");
                Log.d(TAG, "onNext(),first");
                e.onNext("second");
                Log.d(TAG, "onNext(),second");
                e.onNext("third");
                Log.d(TAG, "onNext(),third");
                e.onNext("fourth");
                Log.d(TAG, "onNext(),fourth");
                e.onNext("fifth");
                Log.d(TAG, "onNext(),fifth");
                e.onNext("sixth");
                Log.d(TAG, "onNext(),sixth");
                e.onComplete();
                Log.d(TAG, "onComplete(),observable2");
            }
        });

        // 合并后的事件
        Observable<Person> mergedObservable = Observable.zip(observable1, observable2, new BiFunction<Integer, String, Person>() {
            @Override
            public Person apply(@NonNull Integer age, @NonNull String name) throws Exception {
                // 合并规则
                Person person = new Person();
                person.age = age;
                person.name = name;
                Log.d(TAG, "apply(),person: " + person.toString());
                return person;
            }
        });

        // 合并后的处理。。。
        mergedObservable.subscribe(new Observer<Person>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe()");
            }

            @Override
            public void onNext(@NonNull Person person) {
                Log.d(TAG, "onNext(),person: " + person);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError()");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete(),");
            }
        });
    }

    /**
     * map,对上游发送的每一个事件应用一个函数，使得每一个事件都按照指定的函数去变化
     */
    private static void runMap() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(21);
                Log.d(TAG, "next: " + 21);
                e.onNext(22);
                Log.d(TAG, "next: " + 22);
                e.onComplete();
                Log.d(TAG, "complete ");
            }
        });

        Observable<Person> observable1 = observable.map(new Function<Integer, Person>() {
            int position = 1;

            @Override
            public Person apply(@NonNull Integer num) throws Exception {
                Person person = new Person();
                person.age = num;
                person.name = "name_" + position++;
                Log.d(TAG, "apply(),person: " + person.toString());
                return person;
            }
        });

        observable1.subscribe(new Observer<Person>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe()");
            }

            @Override
            public void onNext(@NonNull Person person) {
                Log.d(TAG, "onNext(),person: " + person.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError()");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()");
            }
        });
    }

    /**
     * flatMap，将上游一个发送事件变换成多个发送事件, in order
     */
    private static void runFlatMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                Log.d(TAG, "next: " + 1);
                e.onNext(2);
                Log.d(TAG, "next: " + 2);
                e.onNext(3);
                Log.d(TAG, "next: " + 3);
                e.onComplete();
                Log.d(TAG, "complete: ");
            }
        }).flatMap(new Function<Integer, ObservableSource<Person>>() {
            @Override
            public ObservableSource<Person> apply(@NonNull Integer integer) throws Exception {
                Log.d(TAG, "apply(): integer: " + integer);
                List<Person> list = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    Person person = new Person();
                    person.age = integer;
                    person.name = "name_" + (integer + i);
                    list.add(person);
                }
                // 延时发送
                return Observable.fromIterable(list).delay(1000, TimeUnit.MILLISECONDS);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Person>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe()");
                    }

                    @Override
                    public void onNext(@NonNull Person person) {
                        Log.d(TAG, "onNext(),person: " + person);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError()");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });
    }

    /**
     * concatMap作用和flatMap几乎一模一样，唯一的区别是它能保证事件的顺序
     */
    private static void runConcatMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                Log.d(TAG, "next: " + 1);
                e.onNext(2);
                Log.d(TAG, "next: " + 2);
                e.onNext(3);
                Log.d(TAG, "next: " + 3);
                e.onComplete();
                Log.d(TAG, "complete: ");
            }
        }).concatMap(new Function<Integer, ObservableSource<Person>>() {
            @Override
            public ObservableSource<Person> apply(@NonNull Integer integer) throws Exception {
                Log.d(TAG, "apply(): integer: " + integer);
                List<Person> list = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    Person person = new Person();
                    person.age = integer;
                    person.name = "name_" + (integer + i);
                    list.add(person);
                }
                // 延时发送
                return Observable.fromIterable(list).delay(1000, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<Person>() {
            @Override
            public void accept(Person person) throws Exception {
                Log.d(TAG, "accept(),person: " + person);
            }
        });
    }

    private static void runDoOnNext() {
        Observable.just(1, 2, 3, 4)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.d(TAG, "doOnNext save " + integer + " success" + "\n");
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.d(TAG, "doOnNext :" + integer + "\n");
                    }
                });
    }

    /**
     * filter，过滤，取符合特定规则的数据
     */
    private static void runFilter() {
        List<Person> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Person person = new Person();
            person.age = i;
            person.name = "name_" + i;
            list.add(person);
        }
        Observable.fromIterable(list)
                .filter(new Predicate<Person>() {
                    @Override
                    public boolean test(@NonNull Person person) throws Exception {
                        return person.age % 2 == 0;
                    }
                })
                .subscribe(new Consumer<Person>() {
                    @Override
                    public void accept(Person person) throws Exception {
                        Log.d(TAG, "accept(),person: " + person.toString());
                    }
                });
    }

    /**
     * skip,代表跳过多少个数目的事件再开始接收
     */
    private static void runSkip() {
        Observable.just("0", "1", "2", "3", "4", "5", "6")
                .skip(3)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "string: " + s);
                    }
                });
    }

    /**
     * take,用于指定订阅者最多收到多少数据
     */
    private static void runTake() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("first");
                Log.d(TAG, "next: first");
                e.onNext("second");
                Log.d(TAG, "next: second");
                e.onNext("third");
                Log.d(TAG, "next: third");
                e.onNext("fourth");
                Log.d(TAG, "next: fourth");
                e.onComplete();
                Log.d(TAG, "complete");
            }
        }).take(2)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "string: " + s);
                    }
                });
    }

    /**
     * timer,替换java中的handler延时执行语句
     */
    private static void runTimer() {
        long time = System.currentTimeMillis();
        Log.d(TAG, "start time: " + time);
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        long acceptTime = System.currentTimeMillis();
                        Log.d(TAG, acceptTime + " " + Thread.currentThread().getName());
                        Log.d(TAG, "interval is " + (acceptTime - time) + ". thread is " + Thread.currentThread().getName());
                    }
                });
    }

    /**
     * interval，替换timer+handler的定时执行操作
     */
    private static void runInterval() {
        long time = System.currentTimeMillis();
        Log.d(TAG, "start time: " + time);
        Observable.interval(1000, 2000, TimeUnit.MILLISECONDS)// 延时1秒，每隔2秒执行一次
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())// 默认新开一个线程，这里指定为主线程执行
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        long acceptTime = System.currentTimeMillis();
                        Log.d(TAG, acceptTime + " " + Thread.currentThread().getName());
                        Log.d(TAG, "interval is " + (acceptTime - time) + ". thread is " + Thread.currentThread().getName());
                    }
                });
    }

    /**
     * concat，连接操作符，可接受Observable的可变参数，或者Observable的集合
     */
    private static void runConcat() {
        Observable.concat(Observable.just(1, 2, 3), Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(4);
                Log.d(TAG, "next: 4");
                e.onComplete();
                Log.d(TAG, "complete");
            }
        })).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept(),integer: " + integer);
            }
        });
    }

    /**
     * distinct，去重操作符
     */
    private static void runDistinct() {
        Observable.just("1", "1", "2", "2", "3", "4", "5")
                .distinct()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept(),value: " + s);
                    }
                });
    }

    /**
     * buffer,分流操作符
     */
    private static void runBuffer() {
        Observable.just(1, 2, 3, 4, 5, 6, 7)
                .buffer(4, 2)// 分别表示单个序列的最大长度，跳过多少个作为下一个序列的起始位置
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        Log.d(TAG, "accept(),list size: " + integers.size());
                        String temp = "";
                        for (int num : integers) {
                            temp += num;
                        }
                        Log.d(TAG, "accept(),integers: " + temp);
                    }
                });
    }

    /**
     * debounce,去抖动，过滤掉发射速率过快的数据项
     */
    private static void runDebounce() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                Thread.sleep(300);
                e.onNext(2);
                Thread.sleep(600);
                e.onNext(3);
                Thread.sleep(200);
                e.onNext(4);
                Thread.sleep(500);
                e.onNext(5);
                Thread.sleep(800);
                e.onComplete();
            }
        }).debounce(500, TimeUnit.MILLISECONDS)// 过滤掉发射速率大于500ms的事件
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept,integer: " + integer);
                    }
                });
    }

    /**
     * defer,每次订阅都会创建一个新的Observable,并且如果该Observable没有被订阅，就不会生成新的Observable
     */
    private static void runDefer() {
        Observable.defer(new Callable<ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> call() throws Exception {
                return Observable.just(1, 2, 3);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe()");
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext(),integer: " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError()");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()");
            }
        });
    }

    /**
     * last，取最后一个值
     */
    private static void runLast() {
        Observable.just(1, 2, 3)
                .last(4)// 默认值，在没有值的时候生效
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept(),integer: " + integer);
                    }
                });
    }

    /**
     * merge,将多个Observable合起来，接受可变参数，也支持使用迭代器集合
     */
    private static void runMerge() {
        Observable.merge(Observable.just(1, 2, 3), Observable.just(4, 5, 6))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept(),integer: " + integer);
                    }
                });
    }

    private static void runReduce() {
        Observable.just(1, 2, 3)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                        Log.d(TAG, "apply(),integer: " + integer + " integer2: " + integer2);
                        return integer + integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept(),integer: " + integer);
                    }
                });
    }

    /**
     * scan，和reducce差不多，区别在于reduce()只输出结果，而scan()会将过程中每一个结果输出
     */
    private static void runScan() {
        Observable.just(1, 2, 3, 4)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                        Log.d(TAG, "apply(),integer: " + integer + " integer2: " + integer2);
                        return integer + integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept(),integer: " + integer);
                    }
                });
    }

    /**
     * window，按照时间划分窗口，将数据发送给不同的Observable
     */
    private static void runWindow() {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(15)
                .window(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Observable<Long>>() {
                    @Override
                    public void accept(Observable<Long> longObservable) throws Exception {
                        Log.d(TAG, "divide...");
                        longObservable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        Log.d(TAG, "accept(),value: " + aLong);
                                    }
                                });
                    }
                });
    }

    /**
     * PublishSubject的使用,PublishSubject与observable的不同在于，PublishSubject可以在创建的时候不指
     * 定数据流（无参数create()方法），并且onNext() 会通知每个观察者
     */
    private static void runPublishSubject() {
        PublishSubject<Integer> subject = PublishSubject.create();

        // 第一次订阅
        subject.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe()--1,isDisposed:" + d.isDisposed());
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext()--1,integer: " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError()--1");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()--1");
            }
        });

        // 灵活指定数据流
        subject.onNext(1);
        subject.onNext(2);
        subject.onNext(3);

        // 第二次订阅
        subject.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe()-2,isDisposed:" + d.isDisposed());
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext()--2,integer: " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError()--2");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()--2");
            }
        });

        // 灵活指定数据流
        subject.onNext(4);
        subject.onNext(5);
        subject.onNext(6);
        subject.onComplete();
    }

    /**
     * AsyncSubject的使用，AsyncSubject在调用 onComplete() 之前，除了 subscribe() 其它的操作都会被缓存，
     * 在调用 onComplete() 之后只有最后一个 onNext() 会生效
     */
    private static void runAsyncSubject() {
        AsyncSubject<Integer> asyncSubject = AsyncSubject.create();

        // 第一次订阅
        asyncSubject.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe()--1,isDisposed:" + d.isDisposed());
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext()--1,integer: " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError()--1");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()--1");
            }
        });

        // 灵活指定数据流
        asyncSubject.onNext(1);
        asyncSubject.onNext(2);
        asyncSubject.onNext(3);

        // 第二次订阅
        asyncSubject.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe()-2,isDisposed:" + d.isDisposed());
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext()--2,integer: " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError()--2");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()--2");
            }
        });

        // 灵活指定数据流
        asyncSubject.onNext(4);
        asyncSubject.onNext(5);
        asyncSubject.onNext(6);
        asyncSubject.onComplete();
    }

    /**
     * BehaviorSubject的使用，BehaviorSubject 的最后一次 onNext() 操作会被缓存，然后在 subscribe() 后立刻推给新注册的 Observer
     */
    private static void runBehaviorSubject() {
        BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();

        // 第一次订阅
        behaviorSubject.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe()--1,isDisposed:" + d.isDisposed());
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext()--1,integer: " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError()--1");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()--1");
            }
        });

        // 灵活指定数据流
        behaviorSubject.onNext(1);
        behaviorSubject.onNext(2);
        behaviorSubject.onNext(3);

        // 第二次订阅
        behaviorSubject.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe()-2,isDisposed:" + d.isDisposed());
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext()--2,integer: " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError()--2");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()--2");
            }
        });

        // 灵活指定数据流
        behaviorSubject.onNext(4);
        behaviorSubject.onNext(5);
        behaviorSubject.onNext(6);
        behaviorSubject.onComplete();
    }

    /**
     * Completable的使用，只关心结果，也就是说 Completable 是没有 onNext 的，要么成功要么出错，不关心过程，在 subscribe 后的某个时间点返回结果
     */
    private static void runCompletable() {
        class MyCompletable implements CompletableOnSubscribe {

            private Person person;

            public MyCompletable(Person person) {
                this.person = person;
            }

            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                if (person == null) {
                    e.onError(new Throwable("数据为空"));
                    return;
                }
                if (person.age < 18) {
                    e.onError(new Throwable("未成年"));
                    return;
                }
                e.onComplete();
            }
        }
        Completable.create(new MyCompletable(new Person(16, "Tom")))
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe(), isDisposed: " + d.isDisposed());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError(),e: " + e.getMessage());
                    }
                });
    }

    /**
     * Flowable，专门用于解决背压问题
     */
    private static void runFlowable() {
        Flowable<Integer> flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                Log.d(TAG, "next 1");
                e.onNext(2);
                Log.d(TAG, "next 2");
                e.onNext(3);
                Log.d(TAG, "next 3");
                e.onNext(4);
                Log.d(TAG, "next 4");
                e.onComplete();
                Log.d(TAG, "complete");
            }
        }, BackpressureStrategy.DROP);
        flowable.reduce(100, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                Log.d(TAG, "integer: " + integer + " integer2: " + integer2);
                return integer + integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "integer: " + integer);
            }
        });
    }
}
