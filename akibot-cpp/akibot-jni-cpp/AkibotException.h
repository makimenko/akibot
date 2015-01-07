/* 
 * File:   AkibotException.h
 * Author: dm
 *
 * Created on January 7, 2015, 11:27 PM
 */

#ifndef AKIBOTEXCEPTION_H
#define	AKIBOTEXCEPTION_H


class AkibotException {
public:
    AkibotException(const std::string& msg) : msg_(msg) {
        fprintf(stderr, "ERROR: %s\n", msg.c_str());
    }
    virtual ~AkibotException() {
    }
    std::string getMessage() const {
        return (msg_);
    }
private:
    std::string msg_;
};





#endif	/* AKIBOTEXCEPTION_H */

