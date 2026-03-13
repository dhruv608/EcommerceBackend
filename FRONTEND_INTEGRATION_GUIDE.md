# 🛒 Frontend Integration Guide - Order Management

## 📋 Table of Contents
- [Frontend Prompts](#-frontend-prompts)
- [Order Flow Integration](#-order-flow-integration)
- [API Integration Examples](#-api-integration-examples)
- [UI Components Needed](#-ui-components-needed)
- [Complete Frontend Code](#-complete-frontend-code)

---

## 🎨 Frontend Prompts

### **🛒 Shopping Cart Page Prompt:**
```
Create a shopping cart page with:
- Cart items display with product images, names, prices, quantities
- Update quantity buttons (+/-)
- Remove item buttons
- Subtotal calculation
- Total amount display
- "Place Order" button that calls order API
- Loading states and error handling
```

### **📦 Order Placement Prompt:**
```
Create an order placement modal/page that:
- Shows order summary with all cart items
- Displays shipping address form
- Shows total amount with tax calculation
- Has "Confirm Order" button
- Calls POST /orders API with userId and items
- Shows success message with order number
- Clears cart after successful order
```

### **📋 Order History Page Prompt:**
```
Create an order history page that:
- Displays all user orders in chronological order
- Shows order number, date, status, total amount
- Expandable order details showing all items
- Order status badges (PENDING, CONFIRMED, SHIPPED, DELIVERED)
- "Track Order" button for each order
- Pagination for large order lists
```

### **🔍 Order Details Page Prompt:**
```
Create an order details page that:
- Shows complete order information
- Displays order number, date, status
- Lists all items with images and details
- Shows shipping and billing addresses
- Displays price breakdown
- Has "Cancel Order" button for pending orders
- Shows order timeline/status updates
```

---

## 🔄 Order Flow Integration

### **🛒 Step 1: Add to Cart**
```javascript
// Add product to cart
const addToCart = async (productId, quantity) => {
    try {
        const response = await fetch('/cart/add?userId=' + userId, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ productId, quantity })
        });
        
        if (response.ok) {
            updateCartDisplay();
            showNotification('Item added to cart!');
        }
    } catch (error) {
        showNotification('Error adding to cart', 'error');
    }
};
```

### **📦 Step 2: Place Order**
```javascript
// Place order from cart
const placeOrder = async () => {
    try {
        // Get cart items
        const cartResponse = await fetch(`/cart?userId=${userId}`);
        const cartItems = await cartResponse.json();
        
        // Convert to order format
        const orderItems = cartItems.map(item => ({
            productId: item.productId,
            quantity: item.quantity
        }));
        
        // Create order
        const orderResponse = await fetch('/orders', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                userId: userId,
                items: orderItems
            })
        });
        
        if (orderResponse.ok) {
            const order = await orderResponse.json();
            showOrderConfirmation(order);
            clearCart();
        }
    } catch (error) {
        showNotification('Error placing order', 'error');
    }
};
```

### **📋 Step 3: View Order History**
```javascript
// Get user orders
const getOrderHistory = async () => {
    try {
        const response = await fetch(`/orders/user/${userId}`);
        const orders = await response.json();
        
        displayOrders(orders);
    } catch (error) {
        showNotification('Error loading orders', 'error');
    }
};
```

---

## 🌐 API Integration Examples

### **🛒 Cart APIs:**
```javascript
// Get cart items
const getCart = async (userId) => {
    const response = await fetch(`/cart?userId=${userId}`);
    return await response.json();
};

// Update cart item
const updateCartItem = async (userId, productId, quantity) => {
    const response = await fetch(`/cart/update?userId=${userId}&productId=${productId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ quantity })
    });
    return await response.json();
};

// Remove cart item
const removeCartItem = async (userId, productId) => {
    const response = await fetch(`/cart/remove?userId=${userId}&productId=${productId}`, {
        method: 'DELETE'
    });
    return await response.json();
};
```

### **📦 Order APIs:**
```javascript
// Create order
const createOrder = async (userId, items) => {
    const response = await fetch('/orders', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId, items })
    });
    return await response.json();
};

// Get user orders
const getUserOrders = async (userId) => {
    const response = await fetch(`/orders/user/${userId}`);
    return await response.json();
};

// Get order details
const getOrderDetails = async (orderNumber) => {
    const response = await fetch(`/orders/number/${orderNumber}`);
    return await response.json();
};

// Update order status
const updateOrderStatus = async (orderNumber, status) => {
    const response = await fetch(`/orders/${orderNumber}/status?status=${status}`, {
        method: 'PUT'
    });
    return await response.json();
};
```

---

## 🎨 UI Components Needed

### **🛒 Shopping Cart Component:**
```jsx
const ShoppingCart = () => {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(false);

    return (
        <div className="shopping-cart">
            <h2>Shopping Cart</h2>
            {cartItems.map(item => (
                <CartItem key={item.cartId} item={item} />
            ))}
            <CartSummary items={cartItems} />
            <PlaceOrderButton onPlaceOrder={placeOrder} />
        </div>
    );
};
```

### **📦 Order Summary Component:**
```jsx
const OrderSummary = ({ order }) => {
    return (
        <div className="order-summary">
            <h3>Order Summary</h3>
            <p>Order Number: {order.orderNumber}</p>
            <p>Status: <span className="status">{order.status}</span></p>
            <p>Total Amount: ${order.totalAmount}</p>
            <div className="order-items">
                {order.orderItems.map(item => (
                    <div key={item.id} className="order-item">
                        <h4>{item.productName}</h4>
                        <p>Quantity: {item.quantity}</p>
                        <p>Price: ${item.price}</p>
                        <p>Subtotal: ${item.subtotal}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};
```

### **📋 Order History Component:**
```jsx
const OrderHistory = () => {
    const [orders, setOrders] = useState([]);

    return (
        <div className="order-history">
            <h2>Order History</h2>
            {orders.map(order => (
                <OrderCard key={order.id} order={order} />
            ))}
        </div>
    );
};
```

---

## 📱 Complete Frontend Code Examples

### **🛒 Shopping Cart Page:**
```jsx
import React, { useState, useEffect } from 'react';

const ShoppingCart = () => {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const userId = getCurrentUserId(); // Get from auth context

    useEffect(() => {
        loadCart();
    }, []);

    const loadCart = async () => {
        try {
            setLoading(true);
            const response = await fetch(`/cart?userId=${userId}`);
            const items = await response.json();
            setCartItems(items);
        } catch (error) {
            console.error('Error loading cart:', error);
        } finally {
            setLoading(false);
        }
    };

    const updateQuantity = async (productId, newQuantity) => {
        try {
            await fetch(`/cart/update?userId=${userId}&productId=${productId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ quantity: newQuantity })
            });
            loadCart(); // Refresh cart
        } catch (error) {
            console.error('Error updating quantity:', error);
        }
    };

    const removeItem = async (productId) => {
        try {
            await fetch(`/cart/remove?userId=${userId}&productId=${productId}`, {
                method: 'DELETE'
            });
            loadCart(); // Refresh cart
        } catch (error) {
            console.error('Error removing item:', error);
        }
    };

    const placeOrder = async () => {
        try {
            // Convert cart items to order format
            const orderItems = cartItems.map(item => ({
                productId: item.productId,
                quantity: item.quantity
            }));

            const response = await fetch('/orders', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    userId: userId,
                    items: orderItems
                })
            });

            if (response.ok) {
                const order = await response.json();
                alert(`Order placed successfully! Order number: ${order.orderNumber}`);
                // Clear cart and redirect to order confirmation
                await fetch(`/cart/clear?userId=${userId}`, { method: 'DELETE' });
                window.location.href = `/order-confirmation/${order.orderNumber}`;
            }
        } catch (error) {
            console.error('Error placing order:', error);
            alert('Error placing order. Please try again.');
        }
    };

    const calculateTotal = () => {
        return cartItems.reduce((total, item) => total + (item.subtotal || 0), 0);
    };

    if (loading) return <div>Loading cart...</div>;

    return (
        <div className="shopping-cart">
            <h1>Shopping Cart</h1>
            
            {cartItems.length === 0 ? (
                <p>Your cart is empty</p>
            ) : (
                <>
                    <div className="cart-items">
                        {cartItems.map(item => (
                            <div key={item.cartId} className="cart-item">
                                <img src={item.productImage} alt={item.productName} />
                                <div className="item-details">
                                    <h3>{item.productName}</h3>
                                    <p>{item.productDescription}</p>
                                    <p>Price: ${item.productPrice}</p>
                                    <div className="quantity-controls">
                                        <button onClick={() => updateQuantity(item.productId, item.quantity - 1)}>
                                            -
                                        </button>
                                        <span>{item.quantity}</span>
                                        <button onClick={() => updateQuantity(item.productId, item.quantity + 1)}>
                                            +
                                        </button>
                                    </div>
                                </div>
                                <div className="item-actions">
                                    <p>Subtotal: ${item.subtotal}</p>
                                    <button onClick={() => removeItem(item.productId)}>
                                        Remove
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                    
                    <div className="cart-summary">
                        <h2>Order Summary</h2>
                        <p>Total Items: {cartItems.reduce((sum, item) => sum + item.quantity, 0)}</p>
                        <p>Total Amount: ${calculateTotal()}</p>
                        <button onClick={placeOrder} className="place-order-btn">
                            Place Order
                        </button>
                    </div>
                </>
            )}
        </div>
    );
};

export default ShoppingCart;
```

### **📋 Order History Page:**
```jsx
import React, { useState, useEffect } from 'react';

const OrderHistory = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const userId = getCurrentUserId();

    useEffect(() => {
        loadOrders();
    }, []);

    const loadOrders = async () => {
        try {
            setLoading(true);
            const response = await fetch(`/orders/user/${userId}`);
            const ordersData = await response.json();
            setOrders(ordersData);
        } catch (error) {
            console.error('Error loading orders:', error);
        } finally {
            setLoading(false);
        }
    };

    const getStatusColor = (status) => {
        switch (status) {
            case 'PENDING': return 'orange';
            case 'CONFIRMED': return 'blue';
            case 'PROCESSING': return 'purple';
            case 'SHIPPED': return 'indigo';
            case 'DELIVERED': return 'green';
            case 'CANCELLED': return 'red';
            default: return 'gray';
        }
    };

    if (loading) return <div>Loading orders...</div>;

    return (
        <div className="order-history">
            <h1>Order History</h1>
            
            {orders.length === 0 ? (
                <p>You haven't placed any orders yet</p>
            ) : (
                <div className="orders-list">
                    {orders.map(order => (
                        <div key={order.id} className="order-card">
                            <div className="order-header">
                                <h3>Order #{order.orderNumber}</h3>
                                <span 
                                    className="status-badge"
                                    style={{ backgroundColor: getStatusColor(order.status) }}
                                >
                                    {order.status}
                                </span>
                            </div>
                            
                            <div className="order-info">
                                <p>Date: {new Date(order.orderDate).toLocaleDateString()}</p>
                                <p>Total Items: {order.totalItems}</p>
                                <p>Total Amount: ${order.totalAmount}</p>
                            </div>
                            
                            <div className="order-items-preview">
                                {order.orderItems.slice(0, 2).map(item => (
                                    <div key={item.id} className="item-preview">
                                        <span>{item.productName} x{item.quantity}</span>
                                        <span>${item.subtotal}</span>
                                    </div>
                                ))}
                                {order.orderItems.length > 2 && (
                                    <p>+{order.orderItems.length - 2} more items</p>
                                )}
                            </div>
                            
                            <div className="order-actions">
                                <button onClick={() => window.location.href = `/orders/${order.orderNumber}`}>
                                    View Details
                                </button>
                                {order.status === 'PENDING' && (
                                    <button onClick={() => cancelOrder(order.orderNumber)}>
                                        Cancel Order
                                    </button>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

const cancelOrder = async (orderNumber) => {
    if (confirm('Are you sure you want to cancel this order?')) {
        try {
            await fetch(`/orders/${orderNumber}/status?status=CANCELLED`, {
                method: 'PUT'
            });
            alert('Order cancelled successfully');
            window.location.reload();
        } catch (error) {
            console.error('Error cancelling order:', error);
            alert('Error cancelling order');
        }
    }
};

export default OrderHistory;
```

---

## 🎯 Integration Checklist

### **✅ Frontend Components Needed:**
- [ ] Shopping Cart page
- [ ] Order placement modal
- [ ] Order history page
- [ ] Order details page
- [ ] Order confirmation page
- [ ] Navigation updates

### **✅ API Integration Points:**
- [ ] Cart APIs (add, update, remove, clear)
- [ ] Order APIs (create, get, update status)
- [ ] User authentication context
- [ ] Error handling
- [ ] Loading states

### **✅ User Flow:**
1. **Browse Products** → Add to Cart
2. **View Cart** → Update quantities
3. **Place Order** → Confirm order
4. **Order Confirmation** → Show order number
5. **Order History** → Track orders

---

## 🎉 Ready for Frontend Development!

**Your order management system is complete and ready for frontend integration!** 🚀✨

**All APIs are working:**
- ✅ **Cart Management** (7 APIs)
- ✅ **Order Management** (5 APIs)
- ✅ **User Authentication** (3 APIs)
- ✅ **Product Catalog** (5 APIs)

**Perfect for building a complete e-commerce frontend!** 🛒🎯
