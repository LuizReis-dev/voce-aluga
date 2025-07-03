const cpfElement = document.getElementById('cpf');
if (cpfElement) {
    IMask(cpfElement, {
        mask: '00000000000'
    });
}

const placaElement = document.getElementById('placa');
if (placaElement) {
    IMask(placaElement, {
        mask: 'aaa0a00',
        prepare: (str) => str.toUpperCase(),
        lazy: false,
        overwrite: true
    });
}

const chassiElement = document.getElementById('chassi');
if (chassiElement) {
    IMask(chassiElement, {
        mask: /^[A-HJ-NPR-Z0-9]{0,17}$/,
        prepare: str => str.toUpperCase(),
        commit: (value, masked) => {
            masked._value = value.toUpperCase();
        }
    });
}

const cepElement = document.getElementById('cep');
if (cepElement) {
    IMask(cepElement, {
        mask: '00000-000'
    });
}