# Brunch Bliss

## Overview
Brunch Bliss is a sophisticated Android application designed to streamline the dining experience for both users and vendors. The app features robust authentication mechanisms, user-friendly interfaces for both user and vendor sides, and secure payment processing via Razorpay. It ensures a seamless and secure experience for all users involved in the brunch ordering and delivery process.

## Features

### Authentication
- **Login**: Users can log in using their email and password.
- **Forgot Password**: Users can reset their password by entering their email, receiving an OTP via mail, and then creating a new password.
- **Signup**: New users can register by providing their email and password, followed by email verification to complete the account creation process.
- **Token Management**: Implements access and refresh tokens, which are stored in datastore<preferences> along with their expiry times for secure session management.

### User Side
- **Canteen Selection**: Users can browse and select from various canteens.
- **Add to Cart**: Users can add items to their cart from the canteen menus.
- **Offers and Categories**: Users can explore available offers, categories, and sub-categories to find their desired items.
- **Payment Gateway**: Integrated with Razorpay for secure and easy online payments. Users receive a QR code upon successful payment, which can be scanned by the canteen for delivery verification.
- **QR Validation**: Delivery verification is made by QR delivered to the user after successful payment
- **Wishlist**: The user can wishlist several items to allow for a more reliable user experience

### Vendor Side
- **Dynamic Menu Management**: Vendors can manage their menu dynamically, adding or removing items as needed.
- **Order Management**: Vendors can view and manage orders placed by users.
- **Payment Verification**: Vendors can scan the QR code provided to users post-payment to verify and process deliveries.
- **Refund Button**: Vendors have the option to issue refunds directly through the app, ensuring a seamless and customer-friendly refund process.

### Additional Features
- **Barcode Scanning and Generation**: The app includes barcode scanning and generation functionality using ZXing, allowing for quick and efficient order processing.
- **Offline Access**: The app provides limited functionality in offline mode, ensuring that users can still view menus and manage orders even without internet access.
- **WishList**: The user can maintain A proper wishlist that can be accessed online 

## Screenshots

### Authentication Screens

<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/7629f9cb-64fe-4550-8966-917217a5fb0f" alt="Login Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/aff2b6c7-b8e2-4899-85d8-dfde489a8e7d" alt="Signup Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/c3f738a4-13d6-4567-b9dc-abdcee7cd87e" alt="Forgot Password Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/18a9ca05-1b65-41f1-8d70-37e1ab31b087" alt="Forgot Password Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/85bbccf7-29ca-4392-aff2-c7b6653aecb8" alt="Forgot Password Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/06d1b6f9-6cde-466a-b10d-c5f1f65476d1" alt="Forgot Password Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/48900631-fc4f-4e8a-99e2-9659e27ab4bf" alt="Forgot Password Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/2db87241-f7b3-4422-b856-ad544bb090c0" alt="Forgot Password Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/066c9911-59ee-484e-8258-ede746514e44" alt="Forgot Password Screen" width="150">

### User Side Screens
<!-- Add screenshots for canteen selection, adding to cart, and payment gateway here -->


<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/b1c70b05-41c9-4df6-b4e6-78d27dfcd65e" alt="Canteen Selection Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/29acae32-727a-4e69-bf70-07fd74ce9529" alt="Add to Cart Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/65375658-a73f-4c7d-8c11-abd9c64ebfd9" alt="Payment Gateway Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/c43962e2-1ca9-4d13-9468-468cf6702b6d" alt="Canteen Selection Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/8ed895ad-e337-445b-8825-c9663e598d81" alt="Add to Cart Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/7d17968a-40e6-4e08-b06d-4770e46144ee" alt="Payment Gateway Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/5249cfda-edc5-4f3d-b65b-90bdc7c00941" alt="Canteen Selection Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/d6bd6d85-a4a3-4778-aa4a-15f3b3fe6f54" alt="Add to Cart Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/0dd5a2d2-9ec6-45b2-a3f9-d3fa869f9187" alt="Payment Gateway Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/fd1f1587-92ef-4fa1-a3f5-8d54eb7943ee" alt="Canteen Selection Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/59a4d946-5b1c-496c-b578-583baf019067" alt="Add to Cart Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/63289f4d-9ffb-4376-af8c-1425aa90ada9" alt="Payment Gateway Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/c4482e34-1fa7-48eb-a3f3-428b3eb9d488" alt="Canteen Selection Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/d0bd418a-3f79-448f-a0ca-9882e0cef685" alt="Add to Cart Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/6dfbd122-0009-472e-a831-f6d165b785ad" alt="Payment Gateway Screen" width="150">
<img src="https://github.com/rockyhappy/CanteenApp/assets/115190222/97c7eba9-4077-4f3c-b9a3-b9144925d3e6" alt="Canteen Selection Screen" width="150">

### Vendor Side Screens
<!-- Add screenshots for dynamic menu management, order management, and refund button here -->
<img src="path/to/dynamic_menu_screenshot.png" alt="Dynamic Menu Management Screen" width="250">
<img src="path/to/order_management_screenshot.png" alt="Order Management Screen" width="250">
<img src="path/to/refund_button_screenshot.png" alt="Refund Button Screen" width="250">

## Dependencies
Brunch Bliss relies on the following key dependencies:
- **Retrofit**: A type-safe HTTP client for Android and Java, Retrofit makes it easy to consume RESTful web services. It handles network requests, parsing responses, and serializing/deserializing data, simplifying API integrations.
- **ZXing**: A barcode image processing library that supports scanning and generating barcodes. It's used for quick and efficient barcode-related functionalities within the app.
- **Razorpay**: For integrating the payment gateway, allowing secure and seamless online payments.
- **Datastore Preferences**: For local storage of tokens and their expiry times.
- **Glide**: For efficient image loading .

## Getting Started
To start using Brunch Bliss:
1. Clone the repository: `git clone https://github.com/your-repo/brunch-bliss-frontend.git`
2. Open the project in Android Studio.
3. Build and run the application on an Android device or emulator.
4. Log in or sign up using your email.
5. Explore the user side to browse canteens and make orders, or switch to the vendor side to manage menus and orders.

## Contributing
Contributions to Brunch Bliss are welcome! To contribute:
1. Fork the repository.
2. Create a new branch for your feature or bug fix: `git checkout -b feature-name`.
3. Make your changes and commit them: `git commit -m 'Add new feature'`.
4. Push to the branch: `git push origin feature-name`.
5. Create a pull request with a detailed description of your changes.

## Contact
For any inquiries or feedback regarding Brunch Bliss, please contact the maintainer.

