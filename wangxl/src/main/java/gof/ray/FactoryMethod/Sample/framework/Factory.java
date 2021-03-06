package gof.ray.FactoryMethod.Sample.framework;

public abstract class Factory {
    // 不允许任何从此类继承的类来覆写这个方法，但是继承仍然可以继承这个方法
    public final Product create(String owner) {
        Product p = createProduct(owner);
        registerProduct(p);
        return p;
    }

    //创建产品
    protected abstract Product createProduct(String owner);
    //注册产品
    protected abstract void registerProduct(Product product);
}
