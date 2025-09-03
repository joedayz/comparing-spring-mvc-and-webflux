// Script de inicialización para MongoDB
// Crear base de datos y colecciones con datos de ejemplo

db = db.getSiblingDB('expenses_demo');

// Crear colección de usuarios
db.createCollection('users');
db.users.insertMany([
    {
        _id: ObjectId(),
        username: 'juan.perez',
        email: 'juan.perez@email.com',
        fullName: 'Juan Pérez',
        createdAt: new Date(),
        balance: 5000.00
    },
    {
        _id: ObjectId(),
        username: 'maria.garcia',
        email: 'maria.garcia@email.com',
        fullName: 'María García',
        createdAt: new Date(),
        balance: 3500.00
    },
    {
        _id: ObjectId(),
        username: 'carlos.lopez',
        email: 'carlos.lopez@email.com',
        fullName: 'Carlos López',
        createdAt: new Date(),
        balance: 8000.00
    }
]);

// Crear colección de categorías de gastos
db.createCollection('categories');
db.categories.insertMany([
    {
        _id: ObjectId(),
        name: 'Alimentación',
        description: 'Gastos en comida y bebidas',
        color: '#FF6B6B'
    },
    {
        _id: ObjectId(),
        name: 'Transporte',
        description: 'Gasolina, transporte público, taxi',
        color: '#4ECDC4'
    },
    {
        _id: ObjectId(),
        name: 'Entretenimiento',
        description: 'Cine, restaurantes, eventos',
        color: '#45B7D1'
    },
    {
        _id: ObjectId(),
        name: 'Servicios',
        description: 'Luz, agua, internet, teléfono',
        color: '#96CEB4'
    },
    {
        _id: ObjectId(),
        name: 'Salud',
        description: 'Medicinas, consultas médicas',
        color: '#FFEAA7'
    }
]);

// Crear colección de gastos
db.createCollection('expenses');
db.expenses.insertMany([
    {
        _id: ObjectId(),
        userId: db.users.findOne({username: 'juan.perez'})._id,
        categoryId: db.categories.findOne({name: 'Alimentación'})._id,
        amount: 45.50,
        description: 'Supermercado para la semana',
        paymentMethod: 'CASH',
        date: new Date('2024-01-15'),
        createdAt: new Date(),
        status: 'COMPLETED'
    },
    {
        _id: ObjectId(),
        userId: db.users.findOne({username: 'juan.perez'})._id,
        categoryId: db.categories.findOne({name: 'Transporte'})._id,
        amount: 25.00,
        description: 'Gasolina',
        paymentMethod: 'DEBIT_CARD',
        date: new Date('2024-01-16'),
        createdAt: new Date(),
        status: 'COMPLETED'
    },
    {
        _id: ObjectId(),
        userId: db.users.findOne({username: 'maria.garcia'})._id,
        categoryId: db.categories.findOne({name: 'Entretenimiento'})._id,
        amount: 120.00,
        description: 'Cena en restaurante',
        paymentMethod: 'CREDIT_CARD',
        date: new Date('2024-01-17'),
        createdAt: new Date(),
        status: 'COMPLETED'
    }
]);

// Crear índices para mejorar el rendimiento
db.users.createIndex({ "username": 1 }, { unique: true });
db.users.createIndex({ "email": 1 }, { unique: true });
db.expenses.createIndex({ "userId": 1, "date": -1 });
db.expenses.createIndex({ "paymentMethod": 1 });
db.expenses.createIndex({ "categoryId": 1 });

print('Base de datos inicializada correctamente con datos de ejemplo');
print('Usuarios: ' + db.users.countDocuments());
print('Categorías: ' + db.categories.countDocuments());
print('Gastos: ' + db.expenses.countDocuments());
