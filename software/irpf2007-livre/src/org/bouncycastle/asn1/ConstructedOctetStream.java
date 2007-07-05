package org.bouncycastle.asn1;

import java.io.InputStream;
import java.io.IOException;

class ConstructedOctetStream
    extends InputStream
{
    private final ASN1ObjectParser _parser;

    private boolean                _first = true;
    private InputStream            _currentStream;

    ConstructedOctetStream(
        ASN1ObjectParser parser)
    {
        _parser = parser;
    }

    public int read()
        throws IOException
    {
        if (_first)
        {
            ASN1OctetStringParser s = (ASN1OctetStringParser)_parser.readObject();

            if (s == null)
            {
                return -1;
            }

            _first = false;
            _currentStream = s.getOctetStream();
        }
        else if (_currentStream == null)
        {
            return -1;
        }

        int b = _currentStream.read();

        if (b < 0)
        {
            ASN1OctetStringParser s = (ASN1OctetStringParser)_parser.readObject();

            if (s == null)
            {
                _currentStream = null;

                return -1;
            }

            _currentStream = s.getOctetStream();

            return _currentStream.read();
        }
        else
        {
            return b;
        }
    }
}
