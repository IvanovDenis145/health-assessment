export const INDICATOR_GROUPS = {
    anthropometric: {
        title: 'Антропометрические показатели',
        icon: '📏',
        indicators: [
            { key: 'heights', title: 'Рост', unit: 'см', fields: [{ name: 'value', label: 'Значение', type: 'number', min: 50, max: 250 }] },
            { key: 'weights', title: 'Вес', unit: 'кг', fields: [{ name: 'value', label: 'Значение', type: 'number', min: 10, max: 300 }] },
            { key: 'chestCircumferences', title: 'Окружность грудной клетки', unit: 'см', fields: [{ name: 'value', label: 'Значение', type: 'number', min: 40, max: 200 }] },
            { key: 'skinFolds', title: 'Толщина кожно-жировой складки', unit: 'мм', fields: [{ name: 'value', label: 'Значение', type: 'number', min: 1, max: 100 }] }
        ]
    },
    physiometric: {
        title: 'Физиометрические показатели',
        icon: '💪',
        indicators: [
            { key: 'vitalLungCapacities', title: 'Жизненная ёмкость лёгких (ЖЕЛ)', unit: 'мл', fields: [{ name: 'value', label: 'Значение', type: 'number', min: 500, max: 8000 }] },
            { key: 'dynamometries', title: 'Кистевая динамометрия', unit: 'кг', fields: [{ name: 'value', label: 'Значение', type: 'number', min: 5, max: 150 }] },
            { key: 'deadliftStrengths', title: 'Становая сила', unit: 'кг', fields: [{ name: 'value', label: 'Значение', type: 'number', min: 10, max: 300 }] }
        ]
    },
    functional: {
        title: 'Функциональное состояние',
        icon: '❤️',
        indicators: [
            { key: 'heartRates', title: 'ЧСС (Пульс)', unit: 'уд/мин', fields: [{ name: 'value', label: 'Значение', type: 'number', min: 30, max: 220 }] },
            { key: 'bloodPressures', title: 'Артериальное давление', unit: 'мм рт. ст.', fields: [
                    { name: 'upperPressure', label: 'Верхнее (систолическое)', type: 'number', min: 50, max: 250 },
                    { name: 'lowerPressure', label: 'Нижнее (диастолическое)', type: 'number', min: 30, max: 150 }
                ]},
            { key: 'respiratoryRates', title: 'Частота дыхания', unit: 'вдохов/мин', fields: [{ name: 'value', label: 'Значение', type: 'number', min: 5, max: 60 }] }
        ]
    },
    lab: {
        title: 'Лабораторные и биохимические маркеры',
        icon: '🧪',
        indicators: [
            { key: 'glucoseLevels', title: 'Уровень глюкозы', unit: 'ммоль/л', fields: [{ name: 'value', label: 'Значение', type: 'number', step: '0.1', min: 1, max: 30 }] },
            { key: 'cholesterolLevels', title: 'Уровень холестерина', unit: 'ммоль/л', fields: [{ name: 'value', label: 'Значение', type: 'number', step: '0.1', min: 1, max: 20 }] },
            { key: 'triglycerideLevels', title: 'Уровень триглицеридов', unit: 'ммоль/л', fields: [{ name: 'value', label: 'Значение', type: 'number', step: '0.1', min: 0.1, max: 20 }] }
        ]
    },
    immune: {
        title: 'Иммунный статус',
        icon: '🛡️',
        indicators: [
            { key: 'immunoglobulinLevels', title: 'Иммуноглобулин G (IgG)', unit: 'г/л', fields: [{ name: 'value', label: 'Значение', type: 'number', step: '0.1', min: 1, max: 50 }] },
            { key: 'phagocyticIndexes', title: 'Фагоцитарный индекс', unit: 'у.е.', fields: [{ name: 'value', label: 'Значение', type: 'number', step: '0.1', min: 0.1, max: 10 }] }
        ]
    },
    mental: {
        title: 'Психическое здоровье',
        icon: '🧠',
        indicators: [
            { key: 'stressLevels', title: 'Уровень стресса', unit: '(1-10)', fields: [{ name: 'value', label: 'Значение', type: 'number', min: 1, max: 10 }] },
            { key: 'mentalDisorders', title: 'Психические расстройства', unit: '', fields: [{ name: 'name', label: 'Название расстройства', type: 'text' }] }
        ]
    },
    subjective: {
        title: 'Субъективное самочувствие',
        icon: '🙂',
        indicators: [
            { key: 'subjectiveWellBeings', title: 'Оценка самочувствия', unit: '(1-10)', fields: [{ name: 'value', label: 'Значение', type: 'number', min: 1, max: 10 }] }
        ]
    }
};