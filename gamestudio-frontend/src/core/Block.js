class Block {
    constructor(shape, color) {
        this.shape = shape;
        this.color = color;
    }

    getWidth() {
        return this.shape[0].length;
    }

    getHeight() {
        return this.shape.length;
    }

    getShape() {
        return this.shape;
    }

    getColor() {
        return this.color;
    }
}

export default Block;