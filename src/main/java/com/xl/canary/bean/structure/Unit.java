package com.xl.canary.bean.structure;

import com.xl.canary.enums.WeightEnum;
import com.xl.canary.enums.loan.LoanOrderElementEnum;
import com.xl.canary.utils.EssentialConstance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * 一捆element, 主要为了兼容多张优惠券优惠同一个元素的情况
 * 同时这样一来, 入账最终的schema只要一个就ok了
 * created by XUAN on 2018/09/22
 */
public class Unit implements List<Element>, Cloneable, Serializable {

    private List<Element> unitList;

    /**
     * 元素
     */
    private LoanOrderElementEnum element;

    public BigDecimal sum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Element element : unitList) {
            sum = sum.add(element.getAmount());
        }
        return sum;
    }

    /**
     * 将百分比惩罚, 优惠整合, 获取占已付金额的百分比
     * @return
     */
    public BigDecimal getCouponPercentInverse() {
        BigDecimal percent = BigDecimal.ONE;
        for (Element elementEntity : unitList) {
            if (elementEntity instanceof CouponElement) {
                CouponElement couponElement = (CouponElement) elementEntity;
                if (couponElement.getWeight().equals(WeightEnum.PERCENT)) {
                    percent = percent.add(couponElement.getAmount());
                }
            }
        }
        if (percent.compareTo(BigDecimal.ONE) != 0) {
            percent = BigDecimal.ONE.divide(BigDecimal.ONE.subtract(percent), EssentialConstance.SCALE, EssentialConstance.UP);
        }
        return percent;
    }

    /**
     * 将数量惩罚, 优惠整合
     * @return
     */
    public BigDecimal getCouponNumber() {
        BigDecimal number = BigDecimal.ZERO;
        for (Element elementEntity : unitList) {
            if (elementEntity instanceof CouponElement) {
                CouponElement couponElement = (CouponElement) elementEntity;
                if (couponElement.getWeight().equals(WeightEnum.NUMBER)) {
                    number = number.add(couponElement.getAmount());
                }
            }
        }
        return number;
    }


    @Override
    public int size() {
        return unitList.size();
    }

    @Override
    public boolean isEmpty() {
        return unitList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return unitList.contains(o);
    }

    @Override
    public Iterator<Element> iterator() {
        return unitList.iterator();
    }

    @Override
    public Object[] toArray() {
        return unitList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return unitList.toArray(a);
    }

    @Override
    public boolean add(Element element) {
        return unitList.add(element);
    }

    @Override
    public boolean remove(Object o) {
        return unitList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return unitList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Element> c) {
        return unitList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Element> c) {
        return unitList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return unitList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return retainAll(c);
    }

    @Override
    public void clear() {
        unitList.clear();
    }

    @Override
    public Element get(int index) {
        return unitList.get(index);
    }

    @Override
    public Element set(int index, Element element) {
        return unitList.set(index, element);
    }

    @Override
    public void add(int index, Element element) {
        unitList.add(index, element);
    }

    @Override
    public Element remove(int index) {
        return unitList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return unitList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return unitList.lastIndexOf(o);
    }

    @Override
    public ListIterator<Element> listIterator() {
        return unitList.listIterator();
    }

    @Override
    public ListIterator<Element> listIterator(int index) {
        return unitList.listIterator(index);
    }

    @Override
    public List<Element> subList(int fromIndex, int toIndex) {
        return unitList.subList(fromIndex, toIndex);
    }

    @Override
    public Unit clone() {
        Unit unit = new Unit();
        unit.element = this.element;
        for (Element element : this.unitList) {
            unit.add(element.clone());
        }
        return unit;
    }


    public Unit () {
        unitList = new ArrayList<Element>();
    }

    public Unit (LoanOrderElementEnum element) {
        unitList = new ArrayList<Element>();
        this.element = element;
    }

    public LoanOrderElementEnum getElement() {
        return element;
    }

    public void setElement(LoanOrderElementEnum element) {
        this.element = element;
    }
}
