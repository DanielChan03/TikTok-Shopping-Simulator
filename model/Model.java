package model;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private User currentUser;
    private List<Product> productCatalog;
    public Model() {
        initializeProductCatalog();
    }
    private void initializeProductCatalog() {
        productCatalog = new ArrayList<>();
        
        // Electronics (Category 1)
        productCatalog.add(new Product("iPhone 16 Pro 256GB", "Apple", 1, 3700, 1));
        productCatalog.add(new Product("Samsung Galaxy S24 Ultra", "Samsung Flagship Store", 37, 3400, 1));
        productCatalog.add(new Product("MacBook Pro 14-inch M3", "Apple", 25, 7000, 1));
        productCatalog.add(new Product("AirPods Pro 3rd Gen", "Apple", 74, 799, 1));
        productCatalog.add(new Product("iPad Air 11-inch", "Apple", 13, 2400, 1));
        productCatalog.add(new Product("Sony WH-1000XM5 Headphones", "Sony Official", 42, 1299, 1));
        productCatalog.add(new Product("Dell XPS 13 Laptop", "Dell Malaysia", 18, 4500, 1));
        productCatalog.add(new Product("SanDisk 128GB USB Drive", "SanDisk", 63, 59.90, 1));
        productCatalog.add(new Product("Canon EOS R6 Camera", "Canon Store", 9, 8800, 1));
        productCatalog.add(new Product("Apple Watch Series 10", "Apple", 56, 1599, 1));
        productCatalog.add(new Product("Samsung 65-inch 4K TV", "Samsung Electronics", 22, 3200, 1));
        productCatalog.add(new Product("Logitech Wireless Mouse", "Logitech", 92, 79.90, 1));
        productCatalog.add(new Product("Anker Power Bank 20000mAh", "Anker", 48, 129, 1));

        // Clothing (Category 2)
        productCatalog.add(new Product("Uniqlo Utility Oversized Shirt", "Uniqlo Malaysia", 27, 149.90, 2));
        productCatalog.add(new Product("Nike Air Force 1 Shoes", "Nike Official", 65, 399, 2));
        productCatalog.add(new Product("Adidas Ultraboost Running Shoes", "Adidas Store", 43, 599, 2));
        productCatalog.add(new Product("H&M Cotton T-Shirt", "H&M Malaysia", 128, 29.90, 2));
        productCatalog.add(new Product("Zara Denim Jacket", "Zara Malaysia", 31, 199, 2));
        productCatalog.add(new Product("Puma Sports Shorts", "Puma Official", 87, 79.90, 2));
        productCatalog.add(new Product("Cotton On Hoodie", "Cotton On Malaysia", 54, 129.90, 2));
        productCatalog.add(new Product("Under Armour Compression Shirt", "Under Armour", 36, 149, 2));
        productCatalog.add(new Product("Levi's 511 Jeans", "Levi's Store", 29, 259, 2));
        productCatalog.add(new Product("Skechers Memory Foam Shoes", "Skechers", 61, 239, 2));

        // Food & Groceries (Category 3)
        productCatalog.add(new Product("Nestle Milo 1kg", "Nestle Malaysia", 200, 18.90, 3));
        productCatalog.add(new Product("Maggi Instant Noodles 5-pack", "Nestle", 150, 5.50, 3));
        productCatalog.add(new Product("Julie's Crackers Assorted", "Julie's Biscuits", 89, 12.90, 3));
        productCatalog.add(new Product("Dutch Lady Milk 1L", "Dutch Lady", 75, 8.90, 3));
        productCatalog.add(new Product("Twinings English Breakfast Tea", "Twinings", 42, 15.90, 3));
        productCatalog.add(new Product("Ferrero Rocher 16pcs", "Ferrero", 56, 35.90, 3));
        productCatalog.add(new Product("Mamee Monster Snack", "Mamee", 120, 2.50, 3));
        productCatalog.add(new Product("Ayam Brand Sardines", "Ayam Brand", 68, 7.90, 3));
        productCatalog.add(new Product("Heinz Tomato Ketchup", "Heinz", 45, 10.90, 3));
        productCatalog.add(new Product("Kit Kat 4-finger", "Nestle", 95, 4.90, 3));

        // Toys & Games (Category 4)
        productCatalog.add(new Product("LEGO Star Wars Millennium Falcon", "LEGO", 15, 899, 4));
        productCatalog.add(new Product("Monopoly Classic Board Game", "Hasbro", 38, 129, 4));
        productCatalog.add(new Product("Nintendo Switch OLED", "Nintendo", 22, 1499, 4));
        productCatalog.add(new Product("PlayStation 5 Console", "Sony", 12, 2199, 4));
        productCatalog.add(new Product("Barbie Dreamhouse", "Mattel", 27, 299, 4));
        productCatalog.add(new Product("Hot Wheels 20-car Set", "Mattel", 84, 89.90, 4));
        productCatalog.add(new Product("Jenga Classic Game", "Hasbro", 56, 49.90, 4));
        productCatalog.add(new Product("Pokemon Trading Cards Set", "Pokemon", 33, 79.90, 4));
        productCatalog.add(new Product("Rubik's Cube 3x3", "Rubik's", 91, 29.90, 4));
        productCatalog.add(new Product("Nerf Elite Disruptor Blaster", "Hasbro", 47, 69.90, 4));

        // Beauty & Personal Care (Category 5)
        productCatalog.add(new Product("Laneige Water Sleeping Mask", "Laneige", 43, 125, 5));
        productCatalog.add(new Product("The Ordinary Niacinamide Serum", "The Ordinary", 67, 45, 5));
        productCatalog.add(new Product("Dove Body Wash 1L", "Dove", 88, 22.90, 5));
        productCatalog.add(new Product("Colgate Total Toothpaste", "Colgate", 120, 12.90, 5));
        productCatalog.add(new Product("Garnier Micellar Water", "Garnier", 54, 18.90, 5));
        productCatalog.add(new Product("Cetaphil Gentle Skin Cleanser", "Cetaphil", 39, 55, 5));
        productCatalog.add(new Product("L'Oreal Paris Hair Serum", "L'Oreal", 62, 35.90, 5));
        productCatalog.add(new Product("Vaseline Intensive Care Lotion", "Vaseline", 71, 15.90, 5));
        productCatalog.add(new Product("Simple Kind to Skin Moisturizer", "Simple", 48, 25.90, 5));
        productCatalog.add(new Product("Nivea Men Face Wash", "Nivea", 59, 19.90, 5));

        // Sports & Outdoors (Category 6)
        productCatalog.add(new Product("Yonex ASTROX 99 PLAY Badminton Racket", "Yonex", 34, 299, 6));
        productCatalog.add(new Product("Adidas Football Size 5", "Adidas", 28, 129, 6));
        productCatalog.add(new Product("Nike Basketball Shoes", "Nike", 41, 459, 6));
        productCatalog.add(new Product("Wilson Tennis Racket", "Wilson", 23, 399, 6));
        productCatalog.add(new Product("Columbia Hiking Jacket", "Columbia", 16, 599, 6));
        productCatalog.add(new Product("Fitbit Charge 6 Fitness Tracker", "Fitbit", 37, 399, 6));
        productCatalog.add(new Product("Yoga Mat Premium", "Decathlon", 52, 89.90, 6));
        productCatalog.add(new Product("Cycling Helmet Safety Certified", "Trek", 44, 159, 6));
        productCatalog.add(new Product("Swimming Goggles Anti-fog", "Speedo", 68, 45.90, 6));
        productCatalog.add(new Product("Camping Tent 4-person", "Coleman", 19, 399, 6));

        // Home & Kitchen (Category 7)
        productCatalog.add(new Product("Tefal Non-stick Frying Pan", "Tefal", 37, 129, 7));
        productCatalog.add(new Product("Philips Air Fryer XXL", "Philips", 24, 599, 7));
        productCatalog.add(new Product("Dyson V11 Cordless Vacuum", "Dyson", 18, 2199, 7));
        productCatalog.add(new Product("Ikea Billy Bookcase", "Ikea", 42, 299, 7));
        productCatalog.add(new Product("KitchenAid Stand Mixer", "KitchenAid", 13, 1599, 7));
        productCatalog.add(new Product("Tupperware Food Container Set", "Tupperware", 76, 89.90, 7));
        productCatalog.add(new Product("Muji Aroma Diffuser", "Muji", 29, 199, 7));
        productCatalog.add(new Product("Dettol Antiseptic Liquid", "Dettol", 95, 15.90, 7));
        productCatalog.add(new Product("Scotch Brite Sponge Pack", "3M", 110, 8.90, 7));
        productCatalog.add(new Product("Toshiba Rice Cooker 1.8L", "Toshiba", 31, 259, 7));

        // Stationary (Category 8)
         // Stationery (Category 8) - Proper stationery items
        productCatalog.add(new Product("Faber-Castell Color Pencils 24pcs", "Faber-Castell", 84, 35.90, 8));
        productCatalog.add(new Product("Casio Scientific Calculator fx-570ES", "Casio", 57, 45.90, 8));
        productCatalog.add(new Product("Pilot G2 Gel Pen 0.7mm 4-pack", "Pilot", 120, 12.90, 8));
        productCatalog.add(new Product("Moleskine Classic Notebook", "Moleskine", 36, 89.90, 8));
        productCatalog.add(new Product("Staedtler Noris Pencil Set", "Staedtler", 75, 18.90, 8));
        productCatalog.add(new Product("3M Post-it Notes 5-pack", "3M", 95, 15.50, 8));
        productCatalog.add(new Product("Artline Whiteboard Marker Set", "Artline", 68, 22.90, 8));
        productCatalog.add(new Product("Bic Cristal Ballpoint Pens 20pcs", "Bic", 110, 14.90, 8));
        productCatalog.add(new Product("Kokuyo Campus Notebook A5", "Kokuyo", 42, 8.90, 8));
        productCatalog.add(new Product("Stabilo Point 88 Fineliner Set", "Stabilo", 53, 39.90, 8));
        productCatalog.add(new Product("Tombow Mono Eraser", "Tombow", 88, 5.90, 8));
        productCatalog.add(new Product("Crayola Washable Markers 10pcs", "Crayola", 61, 25.90, 8));
        productCatalog.add(new Product("Oxford A4 Exercise Book 80pg", "Oxford", 200, 3.90, 8));
        productCatalog.add(new Product("Pentel Mechanical Pencil 0.5mm", "Pentel", 79, 9.90, 8));
        productCatalog.add(new Product("Maped Scissors Student Size", "Maped", 45, 12.90, 8));

        // Others (Category 9)
        productCatalog.add(new Product("Zus Coffee Buy 1 Free 1 Voucher", "SL Empire", 17, 2.99, 9));
        productCatalog.add(new Product("You Tube Premium 1-month Subscription", "TAURUS STUDIO", 12, 17.90, 9));        
        productCatalog.add(new Product("LED Desk Lamp with USB Port", "Philips", 67, 89.90, 9));
        productCatalog.add(new Product("Reusable Shopping Bag Foldable", "Baggu", 89, 12.90, 9));
        productCatalog.add(new Product("Water Bottle Insulated 1L", "Contigo", 74, 49.90, 9));
        productCatalog.add(new Product("Wall Clock Modern Design", "Seiko", 33, 79.90, 9));
        productCatalog.add(new Product("Photo Frame Collage 4-opening", "Ikea", 46, 35.90, 9));
        productCatalog.add(new Product("Plant Succulent in Ceramic Pot", "Green Thumb", 29, 25.90, 9));
        productCatalog.add(new Product("Cable Management Box", "Belkin", 38, 32.90, 9));
        productCatalog.add(new Product("Shoe Rack 5-tier", "Muji", 24, 129, 9));
        productCatalog.add(new Product("Laundry Basket Foldable", "Simplehuman", 55, 69.90, 9));
        productCatalog.add(new Product("First Aid Kit 100-pieces", "Johnson & Johnson", 42, 45.90, 9));
        productCatalog.add(new Product("Umbrella Windproof Automatic", "Euro", 37, 45.90, 9));
        productCatalog.add(new Product("Travel Neck Pillow Memory Foam", "Travel Comfort", 28, 39.90, 9));
        productCatalog.add(new Product("Car Phone Holder Dashboard", "iOttie", 52, 29.90, 9));
    }

    public List<Product> getProductCatalog() {
        return productCatalog;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
   
