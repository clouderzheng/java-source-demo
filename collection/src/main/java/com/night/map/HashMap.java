package com.night.map;

import com.night.map.Map;


public class HashMap<K,V> implements Map<K,V> {

    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;


    /**
     * 当前负载因子
     */
    final float loadFactor;

    /**
     * 默认负载因子
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 数组
     */
    transient Node<K,V>[] table;

    /**
     * 数组的最大容量
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 低于该容量需要扩容
     */
    int threshold;

    /**
     * 链表转红黑树的临界值
     */
    static final int TREEIFY_THRESHOLD = 8;

    transient int modCount;


    transient int size;


    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }

    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {

        Node<K,V>[] tab;
        Node<K,V> p;
        int n ,i ;

        if( (tab = table) == null || (n = table.length) == 0 ){
            n = (tab = resize()).length;
        }

        //数组节点为空
        if( (p = tab[ i = (n - 1) & hash]) == null ){
            tab[i] = new Node<K, V>(hash,key,value,null);
        }else {

            Node<K,V> e = null; K k;
            //当前数组节点就是key 替换
            if(p.hash == hash && ( ( k = p.key) == key || (key != null && key.equals(k)))){
                e = p;
            }else if( p instanceof  TreeNode){
                //插入红黑树
            }else {
                //链表
                for(int binCount = 0 ;; ++binCount){
                    //下一个链表节点为空 挂载上去
                    if( ( e = p.next) == null){
                        p.next = new Node(hash,key,value,null);
                        //链表长度大于转换阀值
                        if(binCount >= TREEIFY_THRESHOLD - 1){
                            //转红黑树
                        }
                        break;
                    }
                    if(p.hash == hash && ( ( k = p.key) == key || (key != null && key.equals(k)))){
                        break;
                    }
                    p = e;
                }
            }


            //e不为空 存在值 替换原值
            if(e != null){
                V oldValue = e.value;
                if(!onlyIfAbsent || oldValue == null){
                    e.value = value;
                }
                return oldValue;
            }
        }

        ++modCount;

        //没有替换 新增了一个节点
        if(++size > threshold){
            resize();
        }
        return null;

    }


    /**
    * 实现功能描述：初始化容器或者扩容2倍容器
    * @Author: zhengjingyun
    * @Date: 2021/7/26
    * @param
    * @return
    */
    final Node<K,V>[] resize(){

        Node<K,V>[] oldtab = table;
        //计算老的容量
        int oldCap = (null == oldtab) ? 0 : oldtab.length;
        //获取老的容量阀值
        int oldThr = threshold;
        int newCap ,newThr = 0;
        if(oldCap > 0){

            //如果老的容量大于等于最大值 则使用最大值
            if(oldCap >= MAXIMUM_CAPACITY ){
                //这里 Integer.MAX_VALUE > MAXIMUM_CAPACITY 个人理解就是threshold已经无效
                //设置一个永远不会达到的值 禁止再扩容
                threshold = Integer.MAX_VALUE;
                return oldtab;
            }
            //如果 老的数组扩容一倍低于最大容量  并且 老的容量大于默认值
            //容量扩容一倍 阀值也扩大一倍
            else if( (newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY){
                newThr = oldThr << 1;
            }
        }
        //针对调用构造函数 指定数组长度的情况
        //默认会计算为大于传入值的最小2的N次方
        else if(oldThr > 0){
            newCap = oldThr;
        }else {
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        }

        if(newThr == 0){
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < MAXIMUM_CAPACITY) ? (int) ft : Integer.MAX_VALUE;
        }
        threshold = newThr;
        Node<K,V>[] newtab = new Node[newCap];
        table = newtab;

        //原表有数据  需要重新计算放入
        if(null != oldtab){

            //遍历老的数组 重新分配
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e ;
                //数组节点不为空 存在链表 或者红黑树
                if( ( e = oldtab[j]) != null){

                    //该索引位的 next为空 只有一个节点 直接rehash 放入newtab
                    if(e.next == null){
                        newtab[ e.hash & (newCap - 1)] = e;
                    }else if( e instanceof TreeNode){
                        //插入红黑树
                    }else {
                        Node<K,V> lohead = null,lotail = null ,hihead = null,hitail = null,next = null;

                        do{
                            //
                            if( ( e.hash & oldCap) == 0){

                                if(lotail == null){
                                    lohead = e;
                                }else {
                                    lotail.next = e;
                                }
                                lotail = e;

                            }else {
                                if(hitail == null){
                                    hihead = e;
                                }else {
                                    hitail.next = e;
                                }
                                hitail = e;
                            }

                        }while (e.next != null);

                        if(lotail != null){
                            lotail = null;
                            newtab[j] = lohead;
                        }
                        if(hitail != null){
                            hitail = null;
                            newtab[ j + oldCap] = hihead;
                        }
                    }
                }
            }
        }

        return newtab;
    }


    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public int size() {
        return 0;
    }

    public V get(K key) {
        return null;
    }

    public V remove(Object key) {
        return null;
    }

    static class Node<K,V> implements Map.Entry<K,V> {

        final int hash;

        final K key;

        V value;

        Node next;

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
        Node(int hash, K key, V value, Node next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        //
        //

        /**
        * 实现功能描述：
         * 异或运算  相同为0 不同为1
         * hash值 低位位移16位 让高位参与运算
        * @Author: zhengjingyun
        * @Date: 2021/7/26
        * @param
        * @return
        */
        static final int hash(Object key) {
            int h;
            return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        }

    }

    static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
        TreeNode<K,V> parent;  // red-black tree links
        TreeNode<K,V> left;
        TreeNode<K,V> right;
        TreeNode<K,V> prev;    // needed to unlink next upon deletion
        boolean red;
        TreeNode(int hash, K key, V value, Node next) {
            super(hash, key, value, next);
        }
    }

}
