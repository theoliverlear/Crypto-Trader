let GlobalScript = require("../../../main/resources/static/script/globalScript");
const assert = require("assert");

const { describe, it } = require("mocha");

describe("encryptPassword", function() {
    it("should return a hashed password", function() {
        assert.strictEqual(GlobalScript.hashPassword("password"), "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
    });
});